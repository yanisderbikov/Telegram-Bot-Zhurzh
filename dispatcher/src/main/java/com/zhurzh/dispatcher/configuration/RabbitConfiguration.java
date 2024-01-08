package com.zhurzh.dispatcher.configuration;

import com.zhurzh.commonrabbitmq.model.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@EnableScheduling
@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(RabbitQueue.TEXT_MESSAGE_UPDATE);
    }

//    @Bean
//    public Queue docMessageQueue() {
//        return new Queue(RabbitQueue.DOC_MESSAGE_UPDATE);
//    }

//    @Bean
//    public Queue photoMessageQueue() {
//        return new Queue(RabbitQueue.PHOTO_MESSAGE_UPDATE);
//    }

    @Bean
    public Queue answerMessageQueue() {
        return new Queue(RabbitQueue.ANSWER_MESSAGE);
    }
    @Bean
    public Queue answerCallbackQueue() {
        return new Queue(RabbitQueue.ANSWER_CALLBACK);
    }
    @Bean
    public Queue dataCallbackQueue() {
        return new Queue(RabbitQueue.CALLBACK_MESSAGE_UPDATE);
    }
//    @Bean
//    public Queue answerPhotoQueue() {
//        return new Queue(RabbitQueue.ANSWER_PHOTO_MESSAGE);
//    }
//    @Bean
//    public Queue editPhotoQueue() {
//        return new Queue(RabbitQueue.EDIT_PHOTO_MESSAGE);
//    }
    @Bean
    public Queue deleteMessageQueue() {
        return new Queue(RabbitQueue.DELETE_MESSAGE_ANSWER);
    }
    @Bean
    public Queue groupTextMessage() {
        return new Queue(RabbitQueue.GROUP_TEXT_MESSAGE_UPDATE);
    }
}
