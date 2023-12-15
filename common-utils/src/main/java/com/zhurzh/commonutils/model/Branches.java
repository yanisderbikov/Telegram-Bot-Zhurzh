package com.zhurzh.commonutils.model;

import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Branches {
//    private String path;
    public ResponseEntity<String> isActive(Update update);
    public ResponseEntity<String> execute(Update update);
}