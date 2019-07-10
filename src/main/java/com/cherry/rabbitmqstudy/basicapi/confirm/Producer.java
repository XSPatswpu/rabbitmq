package com.cherry.rabbitmqstudy.basicapi.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @Auther: cherry
 * @Date: 2019-07-06 11:59
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.5.105");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.获取连接
        Connection connection = connectionFactory.newConnection();

        //3.创建渠道
        Channel channel = connection.createChannel();

        //4.投递消息
        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";
        //指定消息的投递模式为消息确认模式
        channel.confirmSelect();

        String msg = "hello rabbitMQ this is confirm message!";
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());

        //5.添加确认投递的监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("--------ACK!----------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("---------NO ACK!--------------");
            }
        });

    }

}
