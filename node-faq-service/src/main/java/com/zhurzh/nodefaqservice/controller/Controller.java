package com.zhurzh.nodefaqservice.controller;

import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.model.Body;
import com.zhurzh.commonutils.model.Branches;
import com.zhurzh.nodefaqservice.enums.TextMessage;
import com.zhurzh.nodefaqservice.service.NodeFaqService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Log4j
@AllArgsConstructor
public class Controller implements Branches {
    private NodeFaqService nodeFaqService;

    @Override
    @PostMapping("/")
    public ResponseEntity<String> isActive(@RequestBody Body body) {
        var out = TextMessage.ACTIVATION_BUTTON.getMessage(body.getAppUser().getLanguage());
        log.debug("FAQ is active and get");
        return new ResponseEntity<>(out, HttpStatus.OK);
    }


    @Override
    @PostMapping("/execute")
    public ResponseEntity<String> execute(@RequestBody Body body){
        try {
            nodeFaqService.mange(body.getAppUser(), body.getUpdate());
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
