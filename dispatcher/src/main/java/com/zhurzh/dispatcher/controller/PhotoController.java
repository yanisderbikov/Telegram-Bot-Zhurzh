package com.zhurzh.dispatcher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@Log4j
@AllArgsConstructor
public class PhotoController {

    private TelegramBot telegramBot;

    @PostMapping("/photo")
    ResponseEntity<String> sendPhoto(@RequestBody SendPhoto photo){
        var name = photo.getPhoto().getAttachName().substring(9);
        var path = name;
        File f = new File(path); // checking a file
        if (!f.exists()) {
            log.error("File doesnt found ["+path+"]");
            return new ResponseEntity<>(String.format("No file on path %s",path), HttpStatus.BAD_GATEWAY);
        }
        InputFile inputFile = null;
        try {
            inputFile = new InputFile(new FileInputStream(f), path);
        } catch (FileNotFoundException e) {
            log.error("doesn't found the image " + path + " ERROR is : " + e.getMessage());
            return new ResponseEntity<>(String.format("No file on path %s, and error message is: %s", path, e.getMessage()), HttpStatus.BAD_GATEWAY);
        }
        photo.setPhoto(inputFile);
        var send = telegramBot.sendPhoto(photo);
        if (send) return new ResponseEntity<>("Message send", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
}
