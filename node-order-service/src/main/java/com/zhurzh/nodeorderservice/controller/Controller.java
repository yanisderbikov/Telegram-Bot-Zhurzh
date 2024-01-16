package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodeorderservice.enums.TextMessage;
import com.zhurzh.nodeorderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.zhurzh.commonutils.model.Branches;

import java.util.concurrent.CompletableFuture;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {

    private OrderService orderService;
    private CommandsManager cm;

    @Override
    @PostMapping
    public ResponseEntity<String> isActive(@RequestBody Update update){
        var appUser = cm.findOrSaveAppUser(update);
        var out = TextMessage.ACTIVATION_BUTTON.getMessage(appUser.getLanguage());
        return new ResponseEntity<>(out, HttpStatus.OK);
    }
    @Override
    @PostMapping("/execute")
    public CompletableFuture<ResponseEntity<String>> execute(@RequestBody Update update){
        return CompletableFuture.supplyAsync(() -> {
            try {
                orderService.text(update);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                log.error(e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        });
    }
}
