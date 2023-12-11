package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
import com.zhurzh.nodecheckorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ChooseOrderCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    private OrderCasheController os;
    @NonNull
    private final UserState userState = UserState.CHOOSE_ORDER;
    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appUser = cm.findOrSaveAppUser(update);
        if (startCommand(appUser, update)) return;
        if (endCommand(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var orders = orderDAO.findByOwner(appUser).stream()
                    .filter(e -> e.getIsFinished() != null && e.getIsFinished())
                    .toList();
//            cleanAllNullsOrdersName(appUser);
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            if (orders.isEmpty()){
                var out = TextMessage.FAIL_FIND_ORDER.getMessage(appUser.getLanguage());
                cm.addButtonToMainManu(lists);
                cm.sendAnswerEdit(appUser, update, out, lists);
                return true;
            }
            var out = TextMessage.CHOOSE_ORDER_START.getMessage(appUser.getLanguage());
            for (var order : orders){
                cm.addButtonToList(lists, order.getName(), order.getId());
            }
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

    private void cleanAllNullsOrdersName(AppUser appUser) {
        var list = orderDAO.findByOwner(appUser)
                .stream()
                .filter(e -> e.getIsFinished() == null)
                .toList();
        orderDAO.deleteAll(list);
    }

    private boolean endCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()){
            var id = Long.parseLong(update.getCallbackQuery().getData());
            var order = orderDAO.findById(id)
                    .orElseThrow();
            os.setOrder(appUser, order);
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStep(row, appUser, userState);
            var out = TextMessage.CHOOSE_ORDER_END.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }

}
