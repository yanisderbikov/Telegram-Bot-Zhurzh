package com.zhurzh.dispatcher.controller;

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
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private final static String IMAGES_PATH = "dispatcher/src/main/resources/static/images/ZM/";


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
        var message = update.getMessage();

        if (message.hasText()) {
            processTextMessage(update);
	    } else if (message.hasDocument()) {
            processDocMessage(update);
	    } else if (message.hasPhoto()) {
            processPhotoMessage(update);
	    } else {
            setUnsupportedMessageTypeView(update);
	    }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
			"Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
	var sendMessage = messageUtils.generateSendMessageWithText(update,
			"Файл получен! Обрабатывается...");
	    setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setCallback(EditMessageText editMessageText){
        telegramBot.sendCallBack(editMessageText);
    }
    public void setPhoto(SendPhoto photo) {
//        var imagePath = "hello.jpg";
        var name = photo.getPhoto().getAttachName().substring(9);
        var path = IMAGES_PATH + name;
        File f = new File(path);
        if (!f.exists()) log.error("File doesnt found ["+path+"]");
        InputFile inputFile = null;
        try {
            inputFile = new InputFile(new FileInputStream(f), "helloMessage");
        } catch (FileNotFoundException e) {
            log.error("doesn't found the image " + path + " EROOR is : " + e.getMessage());
        }
        photo.setPhoto(inputFile);
        telegramBot.sendPhoto(photo);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
	    updateProducer.produce(DOC_MESSAGE_UPDATE, update);
	    setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processCallbackMessage(Update update){
        updateProducer.produce(CALLBACK_MESSAGE_UPDATE, update);
    }

    public void setEditPhoto(EditMessageMedia editMessageMedia) {
        var path = editMessageMedia.getMedia().getMedia();
        InputMedia media = new InputMediaPhoto();
        media.setMedia(new File(IMAGES_PATH + path), "testImage");
        editMessageMedia.setMedia(media);
        telegramBot.sendEditMessagePhoto(editMessageMedia);
    }

    public void setDeleteMessage(DeleteMessage deleteMessage) {
        telegramBot.sendDeleteMessage(deleteMessage);
    }
}
