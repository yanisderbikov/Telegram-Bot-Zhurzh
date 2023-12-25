package com.zhurzh.dispatcher.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class TelegramBot extends TelegramWebhookBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.uri}")
    private String botUri;
    private final UpdateProcessor updateProcessor;

    public TelegramBot(UpdateProcessor updateProcessor) {
	    this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init() { // 16 урок - исправление на webHook
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/menu", "menu"));

        listOfCommands.add(new BotCommand("/start", "very begin"));
        listOfCommands.add(new BotCommand("/switch_language", "switch_language"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
            e.printStackTrace();
        }
        updateProcessor.registerBot(this);
        try {
            var setWebhook = SetWebhook.builder()
                            .url(botUri)
                            .build();
            this.setWebhook(setWebhook);
            log.debug("botRegistrated");
            log.debug("\nbotUri : " + botUri + "\nbotName : "  + botName + "\nbot Token : " + botToken);
        } catch (TelegramApiException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
	return botName;
    }

    @Override
    public String getBotToken() {
	return botToken;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                message.setParseMode("HTML");
                message.setDisableWebPagePreview(true);
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
    public void sendCallBack(EditMessageText editMessageText){
        if (editMessageText != null){
            try {
                editMessageText.setParseMode("HTML");
                editMessageText.setDisableWebPagePreview(true);
                execute(editMessageText);
            }catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
    public void sendEditMessage(EditMessageMedia editMessageMedia){
        if (editMessageMedia != null){
            try {
                execute(editMessageMedia);
            }catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.debug("web hook called and send null");
        return null;
    }

    public boolean sendPhoto(SendPhoto photo) {
        if (photo != null){
            try {
                execute(photo);
                return true;
            }catch (TelegramApiException e) {
                log.error(e);
                log.debug("URL : " + photo);
                return false;
            }
        }
        return false;
    }
    public boolean sendMedia(SendMediaGroup sendMediaGroup){
        if (sendMediaGroup != null){
            try {
                execute(sendMediaGroup);
                return true;
            }catch (TelegramApiException e) {
                log.error(e);
                log.debug("URL : " + sendMediaGroup);
                return false;
            }
        }
        return false;
    }

    public void sendEditMessagePhoto(EditMessageMedia editMessageMedia) {
        if (editMessageMedia != null){
            try {
                execute(editMessageMedia);
            }catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    public void sendDeleteMessage(DeleteMessage deleteMessage) {
        if (deleteMessage != null){
            try {
                execute(deleteMessage);
            }catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
}
