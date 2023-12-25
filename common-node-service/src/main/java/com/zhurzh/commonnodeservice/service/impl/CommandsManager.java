package com.zhurzh.commonnodeservice.service.impl;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.enums.BranchStatus;
import com.zhurzh.commonnodeservice.service.ProducerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.log4j.Log4j;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
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

    private ConnectionToDispatcherPhoto connectionToDispatcherPhoto;

//    @Value("${file.path}")
    private final String ABSOLUTE_PATH = "/Users/yanderbikovmail.ru/Documents/ProjectsIDE/Telegram/Telegram-Bot-Zhurzh";
    private final String IMAGES_PATH = ABSOLUTE_PATH + "/common-utils/src/main/resources/static/images";
    private final String TELEGRAM_LINK = "http://t.me/";


    public void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, List<List<InlineKeyboardButton>> list) {
        sendAnswerEdit(appUser, update, text, new InlineKeyboardMarkup(list));
    }

    public void sendAnswerEdit(@NotNull Update update, @NotNull String text, @NotNull List<List<InlineKeyboardButton>> list) {
        var appUser = findOrSaveAppUser(update);
        sendAnswerEdit(appUser, update, text, list);
    }

    public void sendAnswerEdit(AppUser appUser, Update update, String text) {
        sendAnswerEdit(appUser, update, text, (InlineKeyboardMarkup) null);
    }

    private void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, InlineKeyboardMarkup markup) {
        if (update == null || !update.hasCallbackQuery()) {
            sendAnswer(appUser, text, markup);
            return;
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage().hasPhoto()){
            deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId());
            log.debug("Message was deleted");
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
        addButtonToMainMenu(list);
        sendAnswerEdit(appUser, update, text, list);
    }

    public boolean sendPhoto(AppUser appUser, Update update, String out, @NotNull String imagePath, List<List<InlineKeyboardButton>> list) {
        long chatId = appUser.getChatId();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(imagePath));
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(out);
        sendPhoto.setReplyMarkup(new InlineKeyboardMarkup(list));

        if (update != null && update.hasMessage()){
            deleteMessage(appUser, update.getMessage().getMessageId());
        } else if (update != null && update.hasCallbackQuery()) {
            deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId());
        }
        var responce = connectionToDispatcherPhoto.sendRequest(sendPhoto);
        return responce.getStatusCode().is2xxSuccessful();
    }

    public boolean sendMedia(AppUser appUser, @NotNull List<InputMedia> medias){

        // Проверяем, есть ли что отправлять
        if (medias.isEmpty()) throw new RuntimeException("Media is empty") ;
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setMedias(medias);
        sendMediaGroup.setChatId(appUser.getChatId());
        var responce = connectionToDispatcherPhoto.sendMedia(sendMediaGroup);
        return responce.getStatusCode().is2xxSuccessful();
    }
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

    private void addButtonToMainMenu(List<List<InlineKeyboardButton>> list) {
        list.add(buttonMainMenu("eng"));
    }

    public void addButtonToMainMenu(List<List<InlineKeyboardButton>> list, AppUser appUser) {
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

    public void addButtonToRowAsURL(List<InlineKeyboardButton> row, String text, String url) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        row.add(button);
    }

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

