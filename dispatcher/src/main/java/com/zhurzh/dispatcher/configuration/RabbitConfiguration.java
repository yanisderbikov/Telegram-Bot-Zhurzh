package com.zhurzh.dispatcher.configuration;

import com.zhurzh.commonrabbitmq.model.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(RabbitQueue.TEXT_TO_SERVER);
    }

    @Bean
    public Queue dataCallbackQueue() {
        return new Queue(RabbitQueue.CALLBACK_TO_SERVER);
    }
    @Bean
    public Queue answerMessageQueue() {
        return new Queue(RabbitQueue.TEXT_TO_TELEGRAM);
    }
    @Bean
    public Queue answerCallbackQueue() {
        return new Queue(RabbitQueue.CALLBACK_TO_TELEGRAM);
    }
    @Bean
    public Queue deleteMessageQueue() {
        return new Queue(RabbitQueue.DELETE_MESSAGE_TO_TELEGRAM);
    }
    @Bean
    public Queue groupTextMessage() {
        return new Queue(RabbitQueue.GROUP_TEXT_MESSAGE_UPDATE);
    }
}
