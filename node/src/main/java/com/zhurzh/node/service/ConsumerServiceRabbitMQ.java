package com.zhurzh.node.service;

import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Интерфейс прослушивает очереди
 */

public interface ConsumerServiceRabbitMQ {
    void consumeTextMessageUpdates(Update update);
//    void consumeDocMessageUpdates(Update update);
//    void consumePhotoMessageUpdates(Update update);
    void consumeCallbackMessageUpdates(Update update);
}
