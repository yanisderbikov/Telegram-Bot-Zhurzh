package com.zhurzh.node.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.node.cache.UserMessageCache;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j
public class ConnectionAppUser {
    private AppUserDAO appUserDAO;

    /**
     *
     * Connect to PostgreSQL
     */
    @Transactional
    public AppUser findOrSaveAppUser(Update update) {
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
                    .branchStatus(BranchStatus.START)
                    .chatId(update.getMessage().getChatId())
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return optional.get();
    }
}

