package com.zhurzh.nodestartservice.controller;

import com.zhurzh.model.Branches;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;


@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {


    @Override
    @GetMapping
    public ResponseEntity<String> isActive(Update update){
        var out = "The branch 'check order service' is online";
        log.debug("update come is active: " + update);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @GetMapping("/callback")
    public ResponseEntity<String> manageCallBack(Update update){
        try {
            log.debug("update come callback manage: " + update);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.IM_USED);
        }
    }
    @Override
    @GetMapping("/text")
    public ResponseEntity<String> manageText(Update update){
        try {
            var out = "text";
            log.debug("update come text manage: " + update);
            return ResponseEntity.ok(out);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}