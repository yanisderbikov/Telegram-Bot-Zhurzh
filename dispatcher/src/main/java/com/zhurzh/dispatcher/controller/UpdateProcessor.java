package com.zhurzh.dispatcher.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.zhurzh.dispatcher.service.UpdateProducer;
import com.zhurzh.dispatcher.utils.MessageUtils;

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
            sendCallbackToServer(update);
            return;
        }
        sendTextToServer(update);
    }


    public void sendTextToTelegram(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void sendCallbackToTelegram(EditMessageText editMessageText) {
        telegramBot.sendCallBack(editMessageText);
    }

    public void sendDeleteMessageToTelegram(DeleteMessage deleteMessage) {
        telegramBot.sendDeleteMessage(deleteMessage);
    }

    private void sendTextToServer(Update update) {
        updateProducer.produce(TEXT_TO_SERVER, update);
    }

    private void sendCallbackToServer(Update update) {
        updateProducer.produce(CALLBACK_TO_SERVER, update);
    }

}
