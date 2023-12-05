package com.zhurzh.demo;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    CommandsManager commandsManager;
    @Autowired
    AppUserDAO appUserDAO;
    @Test
    void contextLoads() {
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        commandsManager.addButtonHelp(list);
        commandsManager.sendAnswerEdit(createUser(), null, "Hello man", list);
        var l = appUserDAO.findAll();
        System.out.println("the list is : ");
        System.out.println(l);

    }
    private AppUser createUser(){
        return AppUser.builder()
                .telegramUserId(1323L)
                .telegramUserName("yanderbikov")
                .chatId(112222L)
                .build();
    }

}
