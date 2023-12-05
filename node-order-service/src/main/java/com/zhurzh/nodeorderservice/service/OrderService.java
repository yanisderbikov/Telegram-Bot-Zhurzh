package com.zhurzh.nodeorderservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;


@Component
@AllArgsConstructor
@Log4j
public class OrderService {
//    private CommandsManager cm;

    public void callback(Update update){
//        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        cm.addButtonToList(list, Message.HI.getMessage(appUser.getLen()), "fds");
//        cm.sendAnswerEdit(appUser, update, Message.BYE.getMessage(appUser.getLen()), list);
        log.debug(String.format("callback hendler: \n%s", update));
    }
    public void text(Update update){
        log.debug(String.format("text hendler: \n%s", update));
    }

}
