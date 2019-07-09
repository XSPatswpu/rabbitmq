package com.cherry.rabbitmqstudy.dlx;

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

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //手动ACK
        if((Integer) properties.getHeaders().get("num") == 0){
            //参数requeue表示是否重回队列
            //注意：在实际生产环境中不会设置重回队列为true
            channel.basicNack(envelope.getDeliveryTag(),false,true);
        }else{
            channel.basicAck(envelope.getDeliveryTag(),false);
        }

    }
}
