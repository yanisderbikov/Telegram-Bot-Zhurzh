package com.zhurzh.dispatcher.service.impl;

import com.zhurzh.dispatcher.controller.UpdateProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
    @RabbitListener(queues = TEXT_TO_TELEGRAM)
    public void consume(SendMessage sendMessage) {
        updateProcessor.sendTextToTelegram(sendMessage);
    }


    @Override
    @RabbitListener(queues = CALLBACK_TO_TELEGRAM)
    public void consume(EditMessageText editMessageText) {
        updateProcessor.sendCallbackToTelegram(editMessageText);
    }

    @Override
    @RabbitListener(queues = DELETE_MESSAGE_TO_TELEGRAM)
    public void consume(DeleteMessage editMessageText) {
        updateProcessor.sendDeleteMessageToTelegram(editMessageText);
    }

}
