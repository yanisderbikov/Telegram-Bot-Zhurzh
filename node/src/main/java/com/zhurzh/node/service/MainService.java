package com.zhurzh.node.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
* Занимается обработкой сообщений полученных из RabbitMQ
 */
public interface MainService {
    void processTextMessage(Update update);
    void processDocMessage(Update update);
    void processPhotoMessage(Update update);
    void processCallBackMessage(Update update);
}
