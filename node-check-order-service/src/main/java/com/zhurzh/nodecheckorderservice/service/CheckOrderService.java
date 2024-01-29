package com.zhurzh.nodecheckorderservice.service;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodecheckorderservice.controller.UserState;
import com.zhurzh.nodecheckorderservice.controller.UserCasheController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j
public class CheckOrderService {
    private CommandsManager cm;
    private UserCasheController us;



    public void mange(Update update){
        try {
            var appUser = cm.findOrSaveAppUser(update);
            var command = us.getCommand(appUser, update);
            command.execute(update);
        }catch (Exception e){
            log.error(e);
            cm.sendToMainMenu(update);
        }
    }
}
