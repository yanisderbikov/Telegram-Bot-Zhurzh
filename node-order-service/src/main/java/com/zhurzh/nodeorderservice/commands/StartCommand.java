package com.zhurzh.nodeorderservice.commands;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.exception.CommandException;
import com.zhurzh.model.Command;
import com.zhurzh.nodeorderservice.controller.HasUserState;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
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
public class StartCommand implements Command, HasUserState {
    private CommandsManager cm;
    private CommonCommands cc;
    @NonNull
    public static final UserState userState = UserState.START;

    @Override
    public UserState getUserState() {
        return userState;
    }

    @Override
    public void execute(Update update) throws CommandException {
        // может приходить /orderservice
        var appUser = cm.findOrSaveAppUser(update);
        if (isMainMessage(appUser, update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }



    private boolean isMainMessage(AppUser appUser, Update update){
        var out = TextMessage.START.getMessage(appUser.getLanguage());
        List<InlineKeyboardButton> row = new ArrayList<>();
        cc.addButtonToNextStepAndCorrectionButton(row, appUser, userState);
        cm.sendAnswerEdit(appUser, update, out, new ArrayList<>(List.of(row)));
        return true;
    }

}
