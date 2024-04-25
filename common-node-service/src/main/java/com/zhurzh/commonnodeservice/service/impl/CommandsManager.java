package com.zhurzh.commonnodeservice.service.impl;

import com.zhurzh.commonnodeservice.service.ProducerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.zhurzh.commonjpa.entity.AppUser;

import javax.validation.constraints.NotNull;
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
    private ConnectionToDispatcherPhoto connectionToDispatcherPhoto;

    public void groupSendAnswer(@NonNull Update update, String out) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(out);
        sendMessage.setChatId(update.getMessage().getChatId());
        sendAnswer(sendMessage);
    }

    public void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, List<List<InlineKeyboardButton>> list) {
        sendAnswerEdit(appUser, update, text, new InlineKeyboardMarkup(list));
    }

    public void sendAnswerEdit(AppUser appUser, Update update, String text) {
        sendAnswerEdit(appUser, update, text, (InlineKeyboardMarkup) null);
    }

    public void sendAnswerEdit(AppUser appUser, Update update, @NotNull String text, InlineKeyboardMarkup markup) {
//        if (checkIsLastMessageAndSave(appUser, update)) return;
        if (update == null || !update.hasCallbackQuery()) {
            sendAnswer(appUser, text, markup);
            return;
        }
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage().hasPhoto()) {
            deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId());
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


    public void deleteMessage(@NotNull AppUser appUser, @NotNull Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(appUser.getChatId()), messageId);
        producerService.producerAnswer(deleteMessage);
    }

    public boolean sendPhoto(AppUser appUser, Update update, String out, @NotNull String imagePath, List<List<InlineKeyboardButton>> list) {
        log.debug("SendPhoto for user : " + appUser.getTelegramUserId());
        long chatId = appUser.getChatId();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(imagePath));
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(out);
        sendPhoto.setProtectContent(true);
        sendPhoto.setReplyMarkup(new InlineKeyboardMarkup(list));

        if (update != null && update.hasMessage()) {
            if (!update.getMessage().getText().equals("/start")) {
                deleteMessage(appUser, update.getMessage().getMessageId());
            }
        } else if (update != null && update.hasCallbackQuery()) {
            deleteMessage(appUser, update.getCallbackQuery().getMessage().getMessageId());
        }
        var responce = connectionToDispatcherPhoto.sendRequest(sendPhoto);
        if (!responce.getStatusCode().is2xxSuccessful()) {
            log.error(responce);
            sendAnswerEdit(appUser, update, out, list);
        }
        log.debug(String.format("SendPhoto via DispatcherPhoto, status : %s, body : %s",
                responce.getStatusCodeValue(), responce.getBody()));
        return responce.getStatusCode().is2xxSuccessful();
    }

    /**
     * Либо отрпавляеет сообщение со всеми данными, либо полностью нет.
     */
    public boolean sendMedia(AppUser appUser, @NotNull List<InputMedia> medias) {

        // Проверяем, есть ли что отправлять
        if (medias.isEmpty()) throw new RuntimeException("Media is empty");
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setMedias(medias);
        sendMediaGroup.setProtectContent(true);
        sendMediaGroup.setChatId(appUser.getChatId());
        var responce = connectionToDispatcherPhoto.sendMedia(sendMediaGroup);
        return responce.getStatusCode().is2xxSuccessful();
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


    public List<InlineKeyboardButton> buttonMainMenu(String len) {
        List<InlineKeyboardButton> list = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText(len.equals("eng") ? "Menu" : "Меню");
        button.setCallbackData("/menu");
        list.add(button);
        return list;
    }

    public void sendToMainMenu(AppUser appUser, Update update) {
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        var isEng = appUser.getLanguage().equals("eng");
        addButtonToList(lists,
                isEng ? "Menu" : "Меню"
                , "/menu");
        sendAnswerEdit(appUser, update, isEng ? "Something wrong. Back to menu?" : "Что-то не так. Вернемся?", lists);
    }

}

