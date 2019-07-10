package com.cherry.rabbitmqstudy.basicapi.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 17:05
 */
public class MyConsumer extends DefaultConsumer {
    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        System.out.println("--------handleDelivery-----------");
        System.out.println("body: " + new String(body));

        //在消费者端进行手动签收
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
