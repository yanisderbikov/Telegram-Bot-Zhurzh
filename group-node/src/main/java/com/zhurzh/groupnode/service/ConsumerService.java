package com.zhurzh.groupnode.service;

import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Интерфейс прослушивает очереди
 */

public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);
}
