package com.zhurzh.groupnode.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
* Занимается обработкой сообщений полученных из RabbitMQ
 */
public interface MainService {
    void processTextMessage(Update update);
}
