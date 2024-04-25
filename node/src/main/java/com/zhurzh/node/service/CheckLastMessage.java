package com.zhurzh.node.service;

import com.zhurzh.node.cache.UserMessageCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class CheckLastMessage {
    private UserMessageCache userMessageCache;
    private ConnectionAppUser connectionAppUser;

    /**
     *
     * Данный метод позволяет определить если в update приходит сообщение, которое выше чем последнее актуальное, то метод возвращает true
     * Если сообщение находятся после последнего актуального, то false, что позволяет все обработать в штатном режиме.
     */
    public boolean checkIsLastMessageAndSave(Update update) {
        if (update == null) return false;
        var appUser = connectionAppUser.findOrSaveAppUser(update);
        var lastMessage = userMessageCache.getLastMessage(appUser);
        Integer curMessage = null;
        if (update.hasMessage()) {
            curMessage = update.getMessage().getMessageId();
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            curMessage = update.getCallbackQuery().getMessage().getMessageId();
        }
        if (lastMessage != null && curMessage != null && lastMessage > curMessage){
            return true;
        }
        if (curMessage != null) userMessageCache.setCache(appUser, curMessage);
        return false;
    }
}
