package com.cherry.rabbitmqstudy.basicapi.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-06-13 22:00
 */
public class Consumer {

    /**
     * 消费者需要从队列中获取消息进行消费
     *
     * 1.创建连接工厂ConnectionFactory
     * 2.获取rabbitMQ的连接connection
     * 3.通过连接开辟通道channel
     * 4.通过channel声明队列queue
     * 5.创建消费者进行消费
     * 6.设置消费者需要监听的队列，以及ACK模式
     * 7.消费
     */
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
//        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
//
//        //6.根据channel设置，消费者需要监听的队列，以及ACK模式
//        channel.basicConsume(queueName,true,queueingConsumer);
//
//        while(true) {
//            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
//            String msg = new String(delivery.getBody());
//            System.out.println("消费者端：" + msg);
//        }
    }
}
