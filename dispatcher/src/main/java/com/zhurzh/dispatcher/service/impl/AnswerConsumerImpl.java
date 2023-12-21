package com.zhurzh.dispatcher.service.impl;

import com.zhurzh.dispatcher.controller.UpdateProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import com.zhurzh.dispatcher.service.AnswerConsumer;


import static com.zhurzh.commonrabbitmq.model.RabbitQueue.*;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    public AnswerConsumerImpl(UpdateProcessor updateProcessor) {
	this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }

    @Override
    @RabbitListener(queues = ANSWER_PHOTO_MESSAGE)
    public void consume(SendPhoto photo) {
        updateProcessor.setPhoto(photo);
    }

    @Override
    @RabbitListener(queues = ANSWER_CALLBACK)
    public void consume(EditMessageText editMessageText) {
        updateProcessor.setCallback(editMessageText);
    }
//    @Override
//    @RabbitListener(queues = EDIT_PHOTO_MESSAGE)
//    public void consume(EditMessageMedia editMessageText) {
//        updateProcessor.setEditPhoto(editMessageText);
//    }

    @Override
    @RabbitListener(queues = DELETE_MESSAGE_ANSWER)
    public void consume(DeleteMessage editMessageText) {
        updateProcessor.setDeleteMessage(editMessageText);
    }

}
