package com.zhurzh.nodesearemservice.controller;

import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.nodesearemservice.service.MenuService;
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

    MenuService menuService;

    @Override
    @PostMapping
    public ResponseEntity<String> isActive(@RequestBody Update update){
        var out = "The branch 'sea rea' is online";
        return new ResponseEntity<>(out, HttpStatus.OK);
    }

    @Override
    @PostMapping("/execute")
    public ResponseEntity<String> execute(@RequestBody Update update){
        try {
            menuService.execute(update);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}