package com.zhurzh.nodestartservice.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.nodestartservice.enums.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class MainNodeStartService {
    @Autowired
    private CommandsManager cm;
    @Autowired
    private AppUserDAO appUserDAO;
    private final String RU = "/ru";
    private final String ENG = "/eng";
    private final String RULES = "/rules";
    private final String WelcomeFunc = "welcomeMessage";
    private final String ToMainMenu = "toMainMenu";
    private Map<AppUser, String> map = new HashMap<>();

//    public MainNodeService(CommandsManager commandsManager, AppUserDAO appUserDAO){
//        this.appUserDAO = appUserDAO;
//        this.cm = commandsManager;
//    }


    public void manageCallBack(Update update){
        var text = update.getCallbackQuery().getMessage().getText();
        var appUser = cm.findOrSaveAppUser(update);
        map.putIfAbsent(appUser, "start");
        switch (map.get(appUser)){
            case WelcomeFunc -> welcomeMessage(update);
            case ToMainMenu -> toMainMenu(update);
            default -> start(update);
        }

    }

    public void manageText(Update update){
        start(update);
    }
    private void start(Update update){
        // switch case ru/eng
        var out = TextMessage.HELLO.getMessage("eng") + "/" + TextMessage.HELLO.getMessage("ru");
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        cm.addButtonToRow(row, "ENG", ENG);
        cm.addButtonToRow(row, "RU", RU);
        lists.add(row);
        cm.sendAnswerEdit(update, out, lists);
        var appUser = cm.findOrSaveAppUser(update);
        map.put(appUser, WelcomeFunc);
    }

    private void welcomeMessage(Update update){
        // manage last message for switch language
        var appUser = cm.findOrSaveAppUser(update);
        if (update.getCallbackQuery().getData().equals(RU)){
            appUser.setLanguage("ru");
        }else {
            appUser.setLanguage("eng");
        }
        appUserDAO.save(appUser);
        var out = TextMessage.WELCOME.getMessage(appUser.getLanguage());
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        cm.addButtonToList(lists, TextMessage.RULES_BUTTON.getMessage(appUser.getLanguage()), RULES);
        cm.sendAnswerEdit(appUser, update, out, lists);
        map.put(appUser, ToMainMenu);
    }

    private void toMainMenu(Update update){
        // manage message with rules
        var appUser = cm.findOrSaveAppUser(update);
        appUser.setBranchStatus(BranchStatus.MENU);
        appUserDAO.save(appUser);
        var out = TextMessage.RULES.getMessage(appUser.getLanguage());
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        cm.addButtonToList(lists, TextMessage.MAIN_MENU_BUTTON.getMessage(appUser.getLanguage()), BranchStatus.MENU.name());
        cm.sendAnswerEdit(appUser, update, out, lists);
        map.remove(appUser);
    }

}
