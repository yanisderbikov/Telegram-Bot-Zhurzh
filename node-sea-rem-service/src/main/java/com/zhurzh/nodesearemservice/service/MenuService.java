package com.zhurzh.nodesearemservice.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
@AllArgsConstructor
public class MenuService {
    private CommandsManager cm;
    private AppUserDAO appUserDAO;

    @Value("${image.hello.url}")
    private String imageHello;

    public void execute(Update update) throws CommandException {
//        var text = update.getCallbackQuery().getMessage().getText();
        var appUser = cm.findOrSaveAppUser(update);
        if (start(update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    private boolean start(Update update){
        // switch case ru/eng

        return false;
    }

}
