package com.zhurzh.model;

import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.commonjpa.entity.AppUser;

public interface Branches {
    public ResponseEntity<String> isActive();
    public ResponseEntity<String> manageCallBack(Update update, AppUser appUser);
    public ResponseEntity<String> manageText(Update update, AppUser appUser);
}

//          ResponseEntity<String>
//          return new ResponseEntity<>(HttpStatus.OK);
//          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);