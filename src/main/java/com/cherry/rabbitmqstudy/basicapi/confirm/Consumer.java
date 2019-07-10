package com.cherry.rabbitmqstudy.basicapi.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-07-06 11:59
 */
public class Consumer {
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

        //4.声明exchange和queue
        String exchangeName = "test_confirm_exchange";
        String exchangeType = "topic";
        String routingKey = "confirm.#";
        String queueName = "test_confirm_queue";

        channel.exchangeDeclare(exchangeName,exchangeType,true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        //5.创建消费者
//        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
//        channel.basicConsume(queueName,true,queueingConsumer);
//
//        while (true){
//            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
//            String message = new String(delivery.getBody());
//            System.out.println(message);
//        }


    }
}
