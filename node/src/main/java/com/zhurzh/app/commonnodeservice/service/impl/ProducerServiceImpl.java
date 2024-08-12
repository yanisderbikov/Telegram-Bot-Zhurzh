package com.zhurzh.app.commonnodeservice.service.impl;

import com.zhurzh.app.commonnodeservice.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import static com.zhurzh.commonrabbitmq.model.RabbitQueue.*;


/**
 * Этот класс запнимается отправкой в RabbitMQ сообщений
 */

@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void producerAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(TEXT_TO_TELEGRAM, sendMessage);
    }
//    @Override
//    public void producerAnswer(SendPhoto sendPhoto) {
//        rabbitTemplate.convertAndSend(ANSWER_PHOTO_MESSAGE, sendPhoto);
//    }
    @Override
    public void producerAnswer(EditMessageText sendMessage) {
        rabbitTemplate.convertAndSend(CALLBACK_TO_TELEGRAM, sendMessage);
    }
//    @Override
//    public void producerAnswer(EditMessageMedia sendMessage) {
//        rabbitTemplate.convertAndSend(EDIT_PHOTO_MESSAGE, sendMessage);
//    }
    @Override
    public void producerAnswer(DeleteMessage sendMessage) {
        rabbitTemplate.convertAndSend(DELETE_MESSAGE_TO_TELEGRAM, sendMessage);
    }


}
