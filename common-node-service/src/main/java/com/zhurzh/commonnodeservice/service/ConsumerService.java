package com.zhurzh.commonnodeservice.service;

import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Интерфейс прослушивает очереди
 */

public interface ConsumerService {
    void consumeTextMessageUpdates(Update update);
    void consumeDocMessageUpdates(Update update);
    void consumePhotoMessageUpdates(Update update);
    void consumeCallbackMessageUpdates(Update update);
}
