package com.zhurzh.commonnodeservice.service.impl;

import com.zhurzh.commonnodeservice.service.MainService;
import com.zhurzh.commonnodeservice.service.ProducerService;
import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Optional;

import static com.zhurzh.commonjpa.entity.UserState.*;

@Log4j
@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    @Override
    public void processCallBackMessage(Update update) {
        try {
            AppUser appUser = findOrSaveAppUser(update);
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder();
            Arrays.stream(e.getStackTrace()).forEach(x -> builder.append("\n").append(x.toString()));
            log.error(builder.toString());
        }
    }

    @Override
    public void processTextMessage(Update update) { // ответы
        try {
            AppUser appUser = findOrSaveAppUser(update);

        } catch (Exception e) {
            StringBuilder builder = new StringBuilder();
            Arrays.stream(e.getStackTrace()).forEach(x -> builder.append("\n").append(x.toString()));
            log.error(builder.toString());
        }
    }


    @Override
    public void processDocMessage(Update update) {
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();

    }

    @Override
    public void processPhotoMessage(Update update) {
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
//
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        var userid = telegramUser.getId();
        Optional<AppUser> optional = Optional.empty();
        try {
            optional = appUserDAO.findByTelegramUserId(userid);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (optional.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .telegramUserName(telegramUser.getUserName())
                    .chatId(update.getMessage().getChatId())
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return optional.get();
    }

}
