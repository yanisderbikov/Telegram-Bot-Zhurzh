package com.zhurzh.nodefaqservice.service;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodefaqservice.controller.UserCasheController;
import com.zhurzh.nodefaqservice.controller.UserState;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Log4j
@AllArgsConstructor
public class NodeFaqService {
    private CommandsManager cm;
    private UserCasheController us;

    public void mange(AppUser appUser, Update update){
        try {
            var command = us.getCommand(appUser, update);
            command.execute(appUser, update);
        }catch (Exception e){
            log.error(e);
            cm.sendToMainMenu(appUser, update);
        }
    }

}
