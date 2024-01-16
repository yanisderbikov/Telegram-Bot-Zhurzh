package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.nodefaqservice.enums.TextMessage;
import com.zhurzh.nodefaqservice.service.NodeFaqService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {
    private CommandsManager cm;
    private NodeFaqService nodeFaqService;

    @Override
    @PostMapping("/")
    public ResponseEntity<String> isActive(@RequestBody Update update) {
        var appUser = cm.findOrSaveAppUser(update);
        var out = TextMessage.ACTIVATION_BUTTON.getMessage(appUser.getLanguage());
        log.debug("FAQ is active and get");
        return new ResponseEntity<>(out, HttpStatus.OK);
    }


    @Override
    @PostMapping("/execute")
    public CompletableFuture<ResponseEntity<String>> execute(@RequestBody Update update){
        return CompletableFuture.supplyAsync(() -> {
            try {
                nodeFaqService.mange(update);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                log.error(e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        });
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
