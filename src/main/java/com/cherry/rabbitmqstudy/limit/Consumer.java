package com.cherry.rabbitmqstudy.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 10:19
 */
public class Consumer {
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
        String exchangeType = "topic";
        String routingKey = "qos.#";
        String queueName = "test_qos_queue";

        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        //限流方式
        //second、设置BasicQos方法
        //参数prefetchCount：MQ服务端每次发送的最大消息数量
        channel.basicQos(0,1,false);
        //first、autoAck设置为false
        channel.basicConsume(queueName,false,new MyConsumer(channel));

        /*
        值得注意的是：basicQos方法和basicConsume方法的执行顺序比较奇怪。
        当basicQos方法后执行时，会出现比较奇怪的现象。对于此现象暂时不予理睬！
         */
    }
}
