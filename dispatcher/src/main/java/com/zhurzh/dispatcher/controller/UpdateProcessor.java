package com.zhurzh.dispatcher.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import com.zhurzh.dispatcher.service.UpdateProducer;
import com.zhurzh.dispatcher.utils.MessageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.zhurzh.commonrabbitmq.model.RabbitQueue.*;


@Component
@Log4j
@Getter
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;


    public UpdateProcessor(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage() || update.hasCallbackQuery()) {
            distributeMessagesByType(update);
        } else {
            log.error("Unsupported message type is received: " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        if (update.hasCallbackQuery()) {
            processCallbackMessage(update);
            return;
        }
        processTextMessage(update);
    }


    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setCallback(EditMessageText editMessageText) {
        telegramBot.sendCallBack(editMessageText);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processCallbackMessage(Update update) {
        updateProducer.produce(CALLBACK_MESSAGE_UPDATE, update);
    }


    public void setDeleteMessage(DeleteMessage deleteMessage) {
        telegramBot.sendDeleteMessage(deleteMessage);
    }
}
