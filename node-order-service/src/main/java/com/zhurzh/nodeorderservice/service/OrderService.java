package com.zhurzh.nodeorderservice.service;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.controller.UserStateController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Component
@AllArgsConstructor
@Log4j
public class OrderService {

    private UserStateController us;
    private CommandsManager cm;

    public void execute(AppUser appUser, Update update) {
        try {
            var command = us.getCommand(appUser, update);
            command.execute(appUser, update);
        }catch (Exception e){
            var list = Arrays.asList(e.getStackTrace());
            log.error(list);
            cm.sendToMainMenu(appUser, update);
        }
    }

}

