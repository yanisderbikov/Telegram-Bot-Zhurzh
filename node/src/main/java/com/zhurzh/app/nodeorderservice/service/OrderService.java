package com.zhurzh.app.nodeorderservice.service;

import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.nodeorderservice.controller.UserStateController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

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
            log.error(List.of(e.getStackTrace()));
            cm.sendToMainMenu(appUser, update);
        }
    }

}

