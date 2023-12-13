package com.zhurzh.dispatcher.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
    void consume(EditMessageText editMessageText);
    void consume(SendPhoto sendPhoto);
    void consume(EditMessageMedia editMessageMedia);
    void consume(DeleteMessage editMessageText);
}
