package com.zhurzh.commonnodeservice.service.impl;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonnodeservice.service.ProducerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.zhurzh.commonjpa.entity.AppUser;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Этот класс помагает легко справляться с любыми возможными сообщениями
 */
@Log4j
@Component
@AllArgsConstructor
public class CommandsManager {
    private ProducerService producerService;
    private AppUserDAO appUserDAO;
    private final String IMAGES_PATH = "dispatcher/src/main/resources/static/images/ZM/";
    private final String TELEGRAM_LINK = "http://t.me/";


    public void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, List<List<InlineKeyboardButton>> list) {
        sendAnswerEdit(appUser, update, text, new InlineKeyboardMarkup(list));
    }
    public void sendAnswerEdit(@NotNull Update update, @NotNull String text, @NotNull List<List<InlineKeyboardButton>> list){
        var appUser = findOrSaveAppUser(update);
        sendAnswerEdit(appUser, update, text, list);
    }

    public void sendAnswerEdit(AppUser appUser, Update update, String text) {
        if (update == null || !update.hasCallbackQuery()) {
            sendAnswer(text, appUser.getChatId());
            return;
        }
        EditMessageText message = new EditMessageText(); // попробовать сразу засетить сюда строку
        message.setChatId(appUser.getChatId());
        message.setText(text);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
//        message.setReplyMarkup(markup);
        sendAnswer(message);
    }

    private void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, InlineKeyboardMarkup markup) {
        if (update == null || !update.hasCallbackQuery()) {
            sendAnswer(appUser, text, markup);
            return;
        }
        EditMessageText message = new EditMessageText(); // попробовать сразу засетить сюда строку
        message.setChatId(appUser.getChatId());
        message.setText(text);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setReplyMarkup(markup);
        sendAnswer(message);
    }

    public void sendEditPhoto(AppUser appUser, Update update, String path, InlineKeyboardMarkup markup) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(appUser.getChatId());
        editMessageMedia.setReplyMarkup(markup);
        editMessageMedia.setMessageId(update.getUpdateId());

        File f = new File(IMAGES_PATH + path);
        if (!f.exists()) {
            log.error("File doesnt found [" + path + "]");
            return;
        }
        InputFile inputFile = null;
        InputMediaPhoto photo = new InputMediaPhoto(path);
        try {
            inputFile = new InputFile(new FileInputStream(f), path);
        } catch (FileNotFoundException e) {
            log.error("doesn't found the image " + path + " ERROR is : " + e.getMessage());
        }
        editMessageMedia.setMedia(photo);
        producerService.producerAnswer(editMessageMedia);
    }

    public void deleteMessage(AppUser appUser, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(appUser.getChatId()), messageId);
        producerService.producerAnswer(deleteMessage);
    }

    public void failMessage(AppUser appUser, Update update) {
        failMessage(appUser, update, "");
    }

    public void failMessage(AppUser appUser, Update update, String message) {

        var text = "Произошла ошибка во время выполнения попробуйте выполнить команду заново. Или сообщите об этом разработчику";
        if (message != null && !message.isEmpty()) {
            text = message;
        }
//        userToDefault(appUser);
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        addButtonToListAsURL(list, tech());
        addButtonToMainManu(list);
        sendAnswerEdit(appUser, update, text, list);
    }

    public void failMessage(AppUser appUser, Update update, Exception e) {
        failMessage(appUser, update);
        StringBuilder builder = new StringBuilder("EXCEPTION in " + appUser.toString() + ": \n\n");
        for (var v : e.getStackTrace()) {
            builder.append(v.toString()).append("\n");
        }
        log.error("Error at program\n", e);
//        sendAnswerEdit(tech(), null, builder.toString());
    }

    public void sendPhoto(AppUser appUser, String imagePath, String caption) {
        long chatId = appUser.getChatId();
        var path = IMAGES_PATH + imagePath;
        File f = new File(path);
        if (!f.exists()) {
            log.error("File doesnt found [" + path + "]");
            return;
        }
        InputFile inputFile = null;
        try {
            inputFile = new InputFile(new FileInputStream(f), imagePath);
        } catch (FileNotFoundException e) {
            log.error("doesn't found the image " + path + " ERROR is : " + e.getMessage());
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);
//        sendPhoto.setReplyMarkup(replyKeyboard);
//        sendPhoto.setReplyToMessageId();
        producerService.producerAnswer(sendPhoto);
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }

    }

    public void sendPhoto(AppUser appUser, String imagePath, String caption, InlineKeyboardMarkup markup) {
        long chatId = appUser.getChatId();
        var path = IMAGES_PATH + imagePath;
        File f = new File(path);
        if (!f.exists()) {
            log.error("File doesnt found [" + path + "]");
            return;
        }
        InputFile inputFile = null;
        try {
            inputFile = new InputFile(new FileInputStream(f), imagePath);
        } catch (FileNotFoundException e) {
            log.error("doesn't found the image " + path + " ERROR is : " + e.getMessage());
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(inputFile);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(markup);
//        sendPhoto.setReplyMarkup(replyKeyboard);
//        sendPhoto.setReplyToMessageId();
        producerService.producerAnswer(sendPhoto);
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }
        for (long i = 0; i < 1000000000; i++) {
        }

    }

    //    public void backToMenu(AppUser appUser, Update update, String message){
//        appUser.setState(BASIC_STATE);
//        appUserDAO.save(appUser);
//        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        list.add(buttonHelp());
//        sendAnswerEdit(appUser, update, message, new InlineKeyboardMarkup(list));
//    }
    public void deleteAllPreviousMessages(AppUser appUser, Update update) {
        Integer id;
        if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getMessage().getMessageId();
        } else {
            id = update.getMessage().getMessageId();
        }
        for (int i = 1; i < 30; i++) {
            deleteMessage(appUser, id - i);
        }
    }

    public void addButtonToMainManu(List<List<InlineKeyboardButton>> list) {
        list.add(buttonMainMenu("eng"));
    }
    public void addButtonToMainManu(List<List<InlineKeyboardButton>> list, AppUser appUser) {
        list.add(buttonMainMenu(appUser.getLanguage()));
    }

    public void addButtonToList(List<List<InlineKeyboardButton>> list, String buttonText, String callbackMessage) {
        var button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(callbackMessage);
        list.add(new ArrayList<>() {{
            add(button);
        }});
    }

    public void addButtonToList(List<List<InlineKeyboardButton>> list, String buttonText, Long callbackMessage) {
        addButtonToList(list, buttonText, String.valueOf(callbackMessage));
    }

    public void addButtonToList(List<List<InlineKeyboardButton>> list, String buttonText, Integer callbackMessage) {
        addButtonToList(list, buttonText, String.valueOf(callbackMessage));
    }

    public void addButtonToListAsURL(List<List<InlineKeyboardButton>> list, AppUser appUser) {
        var button = new InlineKeyboardButton();
        button.setText("Yan Derbikov");
        button.setUrl(TELEGRAM_LINK + appUser.getTelegramUserName());
        list.add(new ArrayList<>() {{
            add(button);
        }});
    }

    public void addButtonToListAsURL(List<List<InlineKeyboardButton>> list, AppUser appUser, String changedName) {
        if (appUser == null) {
            log.error("AppUser is null" + appUser);
            return;
        }
        var button = new InlineKeyboardButton();
        button.setText(String.format(changedName));
        button.setUrl(TELEGRAM_LINK + appUser.getTelegramUserName());
        list.add(new ArrayList<>() {{
            add(button);
        }});
    }

    public void addButtonToListAsLink(List<List<InlineKeyboardButton>> list, String text, String url) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        list.add(new ArrayList<>() {{
            add(button);
        }});
    }

    public void addButtonToRow(List<InlineKeyboardButton> row, String buttonText, String callbackMessage) {
        var button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(callbackMessage);
        row.add(button);
    }

    public void addButtonToRow(List<InlineKeyboardButton> row, String buttonText, Long callbackMessageLong) {
        addButtonToRow(row, buttonText, String.valueOf(callbackMessageLong));
    }

    public void addButtonToRow(List<InlineKeyboardButton> row, String buttonText, Integer callbackMessageLong) {
        addButtonToRow(row, buttonText, String.valueOf(callbackMessageLong));
    }

    //    public void addButtonToRowAsURL(List<InlineKeyboardButton> row, AppUser appUser){
//        var button = new InlineKeyboardButton();
//        button.setText(String.format("%s %s", appUser.getFirstName(), appUser.getLastName()));
//        button.setUrl(TELEGRAM_LINK + appUser.getTelegramUserName());
//        row.add(button);
//    }
//    public AppUser tech(){
//        return AppUser.builder()
//                .firstName("Ян")
//                .lastName("Дербиков")
//                .chatId(331546982L)
//                .telegramUserName("yanderbikov")
//                .build();
//    }
    private void sendAnswer(SendMessage sendMessage) {
        producerService.producerAnswer(sendMessage);
    }

    private void sendAnswer(EditMessageText sendMessage) {
        producerService.producerAnswer(sendMessage);
    }

    private void sendAnswer(AppUser appUser, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(appUser.getChatId());
        sendMessage.setReplyMarkup(replyKeyboard);
        sendAnswer(sendMessage);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    public List<InlineKeyboardButton> buttonMainMenu(String len) {
        List<InlineKeyboardButton> list = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText(len.equals("eng") ? "Menu" : "Меню");
        button.setCallbackData("/menu");
        list.add(button);
        return list;
    }

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

