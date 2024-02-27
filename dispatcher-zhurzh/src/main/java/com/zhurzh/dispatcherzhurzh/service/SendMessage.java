package com.zhurzh.dispatcherzhurzh.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
@AllArgsConstructor
public class SendMessage {
    private AppUserDAO appUserDAO;
    private CommandsManager cm;
    public void sendMessageGroupListUserNames(List<String> telegramIdList, String engMessage, String ruMessage){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        cm.addButtonToList(list, );
        for (var t : telegramIdList){
            var optional = appUserDAO.findByTelegramUserName(t);
            if (optional.isEmpty()) continue;
            var appUser = optional.get();
            var message = appUser.getLanguage().equals("ru") ? ruMessage : engMessage;
            cm.sendAnswerEdit(appUser, null, message, new ArrayList<>(){{add(cm.buttonMainMenu(appUser.getLanguage()));}});
        }
    }

    public void sendMessageGroupListUsersID(List<Long> telegramIdList, String engMessage, String ruMessage){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        cm.addButtonToList(list, );
        for (var t : telegramIdList){
            var optional = appUserDAO.findByTelegramUserId(t);
            if (optional.isEmpty()) continue;
            var appUser = optional.get();
            var message = appUser.getLanguage().equals("ru") ? ruMessage : engMessage;
            cm.sendAnswerEdit(appUser, null, message, new ArrayList<>(){{add(cm.buttonMainMenu(appUser.getLanguage()));}});
        }
    }

    public void sendMessageGroupAppUsers(List<AppUser> appUserList, String engMessage, String ruMessage){
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        cm.addButtonToList(list, );
        for (var appUser : appUserList){
            var message = appUser.getLanguage().equals("ru") ? ruMessage : engMessage;
            cm.sendAnswerEdit(appUser, null, message, list);
        }

    }

    public void sendMessageAllWhoRegitratedOnDate(LocalDateTime localDate, String ru, String eng){
        appUserDAO.findAll()
                .stream()
                .filter(e -> e.getFirstLoginDate().isAfter(localDate))
                .forEach(e -> cm.sendAnswerEdit(e,null, e.getLanguage().equals("eng") ? eng : ru));

    }

}
