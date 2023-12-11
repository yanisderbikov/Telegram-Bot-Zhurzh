package com.zhurzh.demo.controller;

//import com.zhurzh.commonnodeservice.service.MainService;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class DemoController {

//    private MainService cm;
    private final CommandsManager commandsManager;


    @GetMapping
    public String get(){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        commandsManager.addButtonToMainManu(list);
        list.forEach(e -> e.forEach(e2 -> System.out.println(e2)));

//        System.out.println(cm);

//        System.out.println(commandsManager);
        return "Hello";
    }

}
