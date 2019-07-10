package com.cherry.rabbitmqstudy.basicapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 10:19
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.100");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.创建连接
        Connection connection = connectionFactory.newConnection();

        //3.创建渠道
        Channel channel = connection.createChannel();

        //4.根据交换机名称和routingKey发送消息
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        for (int i = 0; i < 1; i++) {
            Map<String,Object> headers = new HashMap<>();
            headers.put("num",i);

            //创建properties
            AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                    .deliveryMode(1) //设置消息投递的模式
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .headers(headers)
                    .build();

            String msg = "hello rabbitMQ DLX Message! " + i;
            channel.basicPublish(exchangeName,routingKey,props,msg.getBytes());
        }

    }
}
