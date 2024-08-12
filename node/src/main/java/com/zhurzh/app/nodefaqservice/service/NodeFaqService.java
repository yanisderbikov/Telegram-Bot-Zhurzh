package com.zhurzh.app.nodefaqservice.service;

import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.app.nodefaqservice.controller.UserCacheController;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@Log4j
@AllArgsConstructor
public class NodeFaqService {
    private CommandsManager cm;
    private UserCacheController us;

    public void mange(AppUser appUser, Update update){
        try {
            var command = us.getCommand(appUser, update);
            command.execute(appUser, update);
        }catch (Exception e){
            log.error(List.of(e.getStackTrace()));
            cm.sendToMainMenu(appUser, update);
        }
    }

}
