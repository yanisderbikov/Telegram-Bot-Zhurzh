package com.zhurzh.nodestartservice.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.commonutils.exception.CommandException;
import com.zhurzh.nodestartservice.enums.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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

    private final String IMAGE = "/start";
    private final String SWITCH_LANGUAGE = "/switch_language";

    @Value("${image.hello.url}")
    private String imageHello;

//    public MainNodeService(CommandsManager commandsManager, AppUserDAO appUserDAO){
//        this.appUserDAO = appUserDAO;
//        this.cm = commandsManager;
//    }


    public void execute(Update update) throws CommandException {
//        var text = update.getCallbackQuery().getMessage().getText();
        var appUser = cm.findOrSaveAppUser(update);
        if (toMainMenu(update)) return;
        if (start(update)) return;
        if (pickLanguageAndRules(update)) return;
        throw new CommandException(Thread.currentThread().getStackTrace());

    }
    private boolean start(Update update){
        // switch case ru/eng
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(SWITCH_LANGUAGE)) {
            var out = TextMessage.SWITCH_LAN.getMessage("eng") + "/" + TextMessage.SWITCH_LAN.getMessage("ru");
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            List<InlineKeyboardButton> row = new ArrayList<>();
            cm.addButtonToRow(row, "ENG", ENG);
            cm.addButtonToRow(row, "RU", RU);
            lists.add(row);
            cm.sendAnswerEdit(update, out, lists);
            var appUser = cm.findOrSaveAppUser(update);
            return true;
        }
        return false;
    }

    private boolean pickLanguageAndRules(Update update){
        if (update.hasMessage() || update.hasCallbackQuery()) {
            // manage last message for switch language
            var appUser = cm.findOrSaveAppUser(update);
            if (update.hasCallbackQuery()) {
                if (update.getCallbackQuery().getData().equals(RU)) {
                    appUser.setLanguage("ru");
                } else {
                    appUser.setLanguage("eng");
                }
            }else {
                var ru = update.getMessage().getFrom().getLanguageCode().equals("ru");
                appUser.setLanguage(ru ? "ru" : "eng");
            }
            appUserDAO.save(appUser);
            var out = TextMessage.WELCOME.getMessage(appUser.getLanguage());
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cm.addButtonToList(lists, TextMessage.RULES_BUTTON.getMessage(appUser.getLanguage()), RULES);

            if (cm.sendPhoto(appUser, update, out, imageHello, lists)) {
                log.debug(String.format("File executed %s", imageHello));
            } else {
                log.error(String.format("File NOT executed %s", imageHello));
                cm.sendAnswerEdit(appUser, update, out, lists);
            }
            return true;
        }
        return false;
    }

    private boolean toMainMenu(Update update){
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(RULES)) {
            // manage message with rules
            var appUser = cm.findOrSaveAppUser(update);
            appUser.setBranchStatus(BranchStatus.MENU);
            appUserDAO.save(appUser);
            var out = TextMessage.RULES.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, update, out);
            List<List<InlineKeyboardButton>> lists = new ArrayList<>();
            cm.addButtonToList(lists, TextMessage.MAIN_MENU_BUTTON.getMessage(appUser.getLanguage()), BranchStatus.MENU.name());
            out = TextMessage.MAIN_MENU_TEXT.getMessage(appUser.getLanguage());
            cm.sendAnswerEdit(appUser, null, out, lists);
            return true;
        }
        return false;
    }

}
