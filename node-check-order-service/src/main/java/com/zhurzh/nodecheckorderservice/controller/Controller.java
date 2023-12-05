package com.zhurzh.nodecheckorderservice.controller;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.model.Branches;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Log4j
public class Controller implements Branches {
    @Override
    @GetMapping("/")
    public ResponseEntity<String> isActive(Update update) {
        var out = "The branch 'check order service' is online";
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @GetMapping("/callback")
    public ResponseEntity<String> manageCallBack(Update update){
        try {
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    @GetMapping("/text")
    public ResponseEntity<String> manageText(Update update){
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
