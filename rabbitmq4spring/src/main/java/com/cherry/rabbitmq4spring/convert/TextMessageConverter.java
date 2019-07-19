package com.cherry.rabbitmq4spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @Auther: cherry
 * @Date: 2019-07-11 20:19
 */
public class TextMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(),messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        MessageProperties messageProperties = message.getMessageProperties();
        String contentType;
        if (messageProperties != null &&
                (contentType = messageProperties.getContentType()) != null && contentType.contains("text")) {
            return new String(message.getBody());
        }
        return message.getBody();
    }
}
