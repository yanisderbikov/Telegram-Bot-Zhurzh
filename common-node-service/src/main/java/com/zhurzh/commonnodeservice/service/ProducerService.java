package com.zhurzh.commonnodeservice.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
/**
 * Этот класс запнимается отправкой в RabbitMQ сообщений
 */

public interface ProducerService {
    void producerAnswer(SendMessage sendMessage);
    void producerAnswer(EditMessageText sendMessage);
//    void producerAnswer(SendPhoto sendPhoto);
//    void producerAnswer(EditMessageMedia editMessageMedia);
    void producerAnswer(DeleteMessage editMessageMedia);

}
