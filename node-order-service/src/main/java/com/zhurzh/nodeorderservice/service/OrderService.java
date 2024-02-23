package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.controller.UserState;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import com.zhurzh.nodeorderservice.ehcache.MyCacheManager;
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

    public void text(Update update) {
        try {
            var appUser = cm.findOrSaveAppUser(update);
            var command = us.getCommand(appUser, update);
            command.execute(update);
        }catch (Exception e){
            var list = Arrays.asList(e.getStackTrace());
            log.error(list);
            cm.sendToMainMenu(update);
        }
    }

}

