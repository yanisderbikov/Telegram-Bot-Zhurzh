package com.zhurzh.dispatcher.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Log4j
public class WebHookController {
    private final UpdateProcessor updateProcessor;

    public WebHookController(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @RequestMapping(value = "/callback/update", method = RequestMethod.POST) // main
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        if (update.hasMessage() && update.getMessage().getChat().getType().equals("private") ||
        update.hasCallbackQuery() && update.getCallbackQuery().getMessage().getChat().getType().equals("private")) {
            updateProcessor.processUpdate(update);
            log.debug("private update");
            return ResponseEntity.ok().build();
        }else {
            log.debug("GROUP MESSAGE : " + update);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
