package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class OrderService {

    private UserStateController us;
    private CommandsManager cm;

    public void callback(Update update) {
        log.debug(String.format("callback handler: \n%s", update));
        try {
            var appUser = cm.findOrSaveAppUser(update);
            var command = us.getCommand(appUser, update);
            command.execute(update);
        }catch (Exception e){
            log.error(getMessage(e));
            sendErrorMessage(update, e);
        }
    }

    public void text(Update update) {
        try {
            log.debug(String.format("text handler: \n%s", update));
            var appUser = cm.findOrSaveAppUser(update);
            var command = us.getCommand(appUser, update);
            command.execute(update);
        }catch (Exception e){
            log.error(getMessage(e));
            sendErrorMessage(update, e);
        }
    }
    private void sendErrorMessage(Update update, Exception e){
        var appUser = cm.findOrSaveAppUser(update);
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        us.setUserState(cm.findOrSaveAppUser(update), UserState.START);
        cm.addButtonToList(lists,
                appUser.getLanguage().equals("eng") ? "Menu" : "Меню"
                , "/menu");
        cm.sendAnswerEdit(appUser, update,
                appUser.getLanguage().equals("ru") ? "Что-то пошло не так" : "Something happened wrong",
                lists);
    }
    private String getMessage(Exception e){
        StringBuilder builder = new StringBuilder();
        Arrays.stream(e.getStackTrace()).forEach(e1 -> builder.append(e1).append("\n"));
        return builder.toString();
    }

}

