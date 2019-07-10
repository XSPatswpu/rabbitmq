package com.cherry.rabbitmqstudy.basicapi.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 10:19
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.102");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.创建连接
        Connection connection = connectionFactory.newConnection();

        //3.创建渠道
        Channel channel = connection.createChannel();

        //4.根据交换机名称和routingKey发送消息
        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";

        for (int i = 0; i < 5; i++) {
            String msg = "hello rabbitMQ QOS Message! " + i;
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        }

    }
}
