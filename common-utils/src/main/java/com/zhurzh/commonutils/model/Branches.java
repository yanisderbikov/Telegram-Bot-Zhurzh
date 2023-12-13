package com.zhurzh.commonutils.model;

import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

public interface Branches {
//    private String path;
    public ResponseEntity<String> isActive(Update update);
    public ResponseEntity<String> manageCallBack(Update update);
    public ResponseEntity<String> manageText(Update update);


}

//          ResponseEntity<String>
//          return new ResponseEntity<>(HttpStatus.OK);
//          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);