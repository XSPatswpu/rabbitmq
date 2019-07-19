package com.cherry.rabbitmqstudy.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 17:05
 */
public class MyConsumer extends DefaultConsumer {
    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        System.out.println("--------handleDelivery-----------");
        System.out.println("consumerTag: " + consumerTag);
        System.out.println("envelope: " + envelope);
        System.out.println("properties: " + properties);
        System.out.println("body: " + new String(body));

    }
}
