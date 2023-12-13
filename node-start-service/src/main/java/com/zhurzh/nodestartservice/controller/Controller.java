package com.zhurzh.nodestartservice.controller;

import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.nodestartservice.service.MainNodeStartService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;


@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {

    MainNodeStartService mainNodeStartService;

    @Override
    @PostMapping
    public ResponseEntity<String> isActive(@RequestBody Update update){
        var out = "The branch 'check order service' is online";
        log.debug("update come is active: " + update);
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @PostMapping("/callback")
    public ResponseEntity<String> manageCallBack(@RequestBody Update update){
        try {
            log.debug("update come callback manage: " + update);
            mainNodeStartService.manageCallBack(update);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    @PostMapping("/text")
    public ResponseEntity<String> manageText(@RequestBody Update update){
        try {
            var out = "text";
            log.debug("update come text manage: " + update);
            mainNodeStartService.manageText(update);
            return ResponseEntity.ok(out);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}