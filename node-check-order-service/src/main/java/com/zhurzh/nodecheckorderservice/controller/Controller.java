package com.zhurzh.nodecheckorderservice.controller;

import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.model.Branches;
import com.zhurzh.nodecheckorderservice.enums.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {
    private CommandsManager cm;
    @Override
    @PostMapping("/")
    public ResponseEntity<String> isActive(@RequestBody Update update) {
        var appUser = cm.findOrSaveAppUser(update);
        var out = TextMessage.ACTIVATION_BUTTON.getMessage(appUser.getLanguage());
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @PostMapping("/callback")
    public ResponseEntity<String> manageCallBack(@RequestBody Update update){
        try {
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    @PostMapping("/text")
    public ResponseEntity<String> manageText(@RequestBody Update update){
        try {
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
