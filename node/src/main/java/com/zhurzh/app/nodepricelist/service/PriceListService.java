package com.zhurzh.app.nodepricelist.service;

import com.zhurzh.app.nodepricelist.commands.PriceListCommand;
import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonnodeservice.service.impl.CommandsManager;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
@AllArgsConstructor
public class PriceListService {

    private PriceListCommand command;
    private CommandsManager cm;
    public void manage(AppUser appUser, Update update){
        try {
            command.execute(appUser, update);
        }catch (Exception e){
            log.error(e);
            cm.sendToMainMenu(appUser, update);
        }
    }
}
