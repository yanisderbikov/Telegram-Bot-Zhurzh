package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.commonnodeservice.model.Branches;
import com.zhurzh.nodeorderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {

    private OrderService orderService;

    @Override
    @GetMapping
    public boolean isActive(){
        return true;
    }

    @Override
    @GetMapping("/callback")
    public boolean manageCallBack(Update update, AppUser appUser){
        try {
            orderService.callback(update, appUser);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
    @Override
    @GetMapping("/text")
    public boolean manageText(Update update, AppUser appUser){
        try {
            orderService.text(update, appUser);
            return true;
        }catch (Exception e){
            log.error(e);
            return false;
        }
    }
}
