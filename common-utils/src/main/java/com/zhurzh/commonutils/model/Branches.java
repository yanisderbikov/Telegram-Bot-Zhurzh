package com.zhurzh.commonutils.model;

import com.zhurzh.commonjpa.entity.AppUser;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Branches {
//    private String path;
    ResponseEntity<String> isActive(Body body);
    ResponseEntity<String> execute(Body body);
}