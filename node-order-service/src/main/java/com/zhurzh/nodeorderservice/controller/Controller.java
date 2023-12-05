package com.zhurzh.nodeorderservice.controller;

import com.zhurzh.nodeorderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

import com.zhurzh.model.Branches;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {

    private OrderService orderService;

    @Override
    @PostMapping
    public ResponseEntity<String> isActive(@RequestBody Update update){
        var out = "The branch 'node order service' is online";
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @PostMapping("/callback")
    public ResponseEntity<String> manageCallBack(@RequestBody Update update){
        try {
            orderService.callback(update);
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
            orderService.text(update);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
