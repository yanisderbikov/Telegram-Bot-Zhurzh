package com.zhurzh.nodecheckorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.commonutils.model.Command;
import com.zhurzh.nodecheckorderservice.controller.HasUserState;
import com.zhurzh.nodecheckorderservice.controller.OrderCasheController;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
import com.zhurzh.nodecheckorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseOrderCommand implements Command, HasUserState {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private CommonCommands cc;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderCasheController os;
    @Value("${image.path.empty.order}")
    private String imagePathEmptyOrder;

    @Value("${image.path.found.order}")
    private String imagePathFoundOrder;

    @NonNull
    public static final UserState userState = UserState.CHOOSE_ORDER;
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

    @Override
    public boolean isExecuted(AppUser appUser) {
        return false;
    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var orders = orderDAO.findByOwner(appUser).stream()
                    .filter(e -> e.getIsFinished() != null && e.getIsFinished())
                    .toList();
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            if (orders.isEmpty()){
                var out = TextMessage.FAIL_FIND_ORDER.getMessage(appUser.getLanguage());
                cm.addButtonToMainMenu(lists, appUser);
                cm.sendPhoto(appUser, update, out, imagePathEmptyOrder, lists);
                return true;
            }
            var out = TextMessage.CHOOSE_ORDER_START.getMessage(appUser.getLanguage());
            for (var order : orders){
                cm.addButtonToList(lists, order.getName(), order.getId());
            }
            cm.sendPhoto(appUser, update, out, imagePathFoundOrder, lists);
            return true;
        }
        return false;
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
