package com.zhurzh.nodecheckorderservice.service;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@AllArgsConstructor
@Log4j
public class CommonCommands {
    private CommandsManager cm;
    private OrderDAO orderDAO;


    public void addButtonToNextStep(List<InlineKeyboardButton> row, AppUser appUser,
                                    UserState userState) throws RuntimeException{
        var currentState = userState.ordinal();
        if (currentState + 1 >= UserState.values().length) {
            var out = "This was a final step in UserState : "
                    + userState.getMessage(appUser.getLanguage());
            log.warn(out);
            row = cm.buttonMainMenu(appUser.getLanguage());
            return;
        }
        var nextState = UserState.values()[currentState + 1];
        cm.addButtonToRow(row, nextState.getMessage(appUser.getLanguage()),
                nextState.getPath());

    }

}
