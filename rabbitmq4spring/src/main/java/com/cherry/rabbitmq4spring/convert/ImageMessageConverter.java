package com.cherry.rabbitmq4spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: cherry
 * @Date: 2019-07-15 17:02
 */
public class ImageMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(),messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("---------Image Converter---------------");
        MessageProperties properties = message.getMessageProperties();
        Map<String,Object> headers = properties.getHeaders();
        //文件后缀名
        String extName = "png";
        Object extName0;
        if(headers != null && (extName0 = headers.get("extName")) != null){
            extName = extName0.toString();
        }

        //IO操作 内存 -> 磁盘
        String path = "/Users/cherry/" + UUID.randomUUID() + "." + extName;
        File file = new File(path);
        try {
            Files.copy(new ByteArrayInputStream(message.getBody()),file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
