package com.cherry.rabbitmqstudy.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.Map;

/**
 * @Auther: cherry
 * @Date: 2019-06-26 15:15
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂，并配置属性
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机IP
        connectionFactory.setHost("192.168.0.102");
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/");

        //2.获取连接
        Connection connection = connectionFactory.newConnection();

        //3.通过连接开辟通道
        Channel channel = connection.createChannel();

        //4.通过channel声明一个队列
        String queueName = "cherry001";
        channel.queueDeclare(queueName,true,false,false,null);

        //5.创建消费者进行消费
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6.根据channel设置，消费者需要监听的队列，以及ACK模式
        channel.basicConsume(queueName,true,queueingConsumer);

        while(true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            Map<String,Object> header = delivery.getProperties().getHeaders();
            System.out.println("消费者端：" + msg + ",get the header: " + header.toString());
        }
    }
}
