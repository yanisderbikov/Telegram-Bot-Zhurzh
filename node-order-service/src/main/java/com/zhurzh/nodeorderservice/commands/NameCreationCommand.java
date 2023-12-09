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

@AllArgsConstructor
@Component
public class NameCreationCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    private OrderDAO orderDAO;
    @NonNull
    public static final UserState userState = UserState.NAME;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        var appuser = cm.findOrSaveAppUser(update);
        if (startCommand(appuser, update)) return;
        if (endCommand(appuser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }

    private boolean startCommand(AppUser appUser, Update update){
        if (update.hasCallbackQuery()){
            if (!update.getCallbackQuery().getData().equals(userState.getPath())) return false;
            var out = TextMessage.NAME_START.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            return true;
        }
        return false;
    }

    private boolean endCommand(AppUser appUser, Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            var input = update.getMessage().getText();
            var order = cc.findActiveOrder(appUser);
            order.setName(input);
            orderDAO.save(order);
            var out = TextMessage.NAME_END.getMessage(appUser.getLanguage());
            List<InlineKeyboardButton> row = new ArrayList<>();
            cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
            cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
            return true;
        }
        return false;
    }
}
