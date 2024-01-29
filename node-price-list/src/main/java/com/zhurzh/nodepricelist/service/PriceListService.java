package com.zhurzh.nodepricelist.service;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodepricelist.commands.PriceListCommand;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
@AllArgsConstructor
public class PriceListService {

    private PriceListCommand command;
    private CommandsManager cm;
    public void manage(Update update){
        try {
            command.execute(update);
        }catch (Exception e){
            log.error(e);
            cm.sendToMainMenu(update);
        }
    }
}
