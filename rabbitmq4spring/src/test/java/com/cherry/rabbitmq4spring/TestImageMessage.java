package com.cherry.rabbitmq4spring;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Auther: cherry
 * @Date: 2019-07-17 07:47
 */
public class TestImageMessage extends AppTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testImage2Message() throws IOException {

        /*
        //IO操作 磁盘 -> 内存
        byte[] imageBytes = Files.readAllBytes(Paths.get("/Users/cherry/Desktop","test1.jpg"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("extName","jpg");
        //修改contentType
        messageProperties.setContentType("application/image");
        Message message = new Message(imageBytes,messageProperties);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message);
        */


        //IO操作 磁盘 -> 内存
        byte[] imageBytes = Files.readAllBytes(Paths.get("/Users/cherry/Desktop","test2.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("extName","pdf");
        //修改contentType
        messageProperties.setContentType("application/pdf");
        Message message = new Message(imageBytes,messageProperties);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message);
    }
}
