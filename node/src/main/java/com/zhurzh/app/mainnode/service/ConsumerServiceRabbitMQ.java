package com.zhurzh.app.mainnode.service;

import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Интерфейс прослушивает очереди
 */

public interface ConsumerServiceRabbitMQ {
    void consumeTextMessageUpdates(Update update);
    void consumeCallbackMessageUpdates(Update update);
}
