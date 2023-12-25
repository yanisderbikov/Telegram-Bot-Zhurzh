package com.zhurzh.dispatcher.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;


import java.util.ArrayList;
import java.util.List;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j
@AllArgsConstructor
public class PhotoController {

    private TelegramBot telegramBot;

    @PostMapping("/photo")
    ResponseEntity<String> sendPhoto(@RequestBody SendPhoto photo) {
        var send = telegramBot.sendPhoto(photo);
        if (send) return new ResponseEntity<>("Message send", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @PostMapping("/media")
    ResponseEntity<String> sendPhoto(@RequestBody SendMediaGroup sendMediaGroup) {
        var send = telegramBot.sendMedia(sendMediaGroup);
        if (send) return new ResponseEntity<>("Message send", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

}
