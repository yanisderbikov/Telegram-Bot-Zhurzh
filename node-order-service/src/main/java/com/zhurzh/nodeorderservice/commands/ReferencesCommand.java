package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.CommonCommands;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ReferencesCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    @NonNull
    public static final UserState userState = UserState.REFERENCES;

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

    private boolean startCommand(AppUser appUser, Update update) {
        if (update.hasCallbackQuery()) {
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.REFERENCE_START.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            if (!isLinkIsValid(input)){
                // TODO: 08/12/23 Добавить реализацию проверки ссылки
                return false;
            }
            var order = cc.findActiveOrder(appUser);
            order.setReference(input);
            orderDAO.save(order);

            List<InlineKeyboardButton> row = new ArrayList<>();
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            lists.add(row);
            var out = TextMessage.REFERENCE_END.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out, lists);
            return true;
        }
        return false;
    }

    private boolean isLinkIsValid(String url){
        return true;
    }
}
