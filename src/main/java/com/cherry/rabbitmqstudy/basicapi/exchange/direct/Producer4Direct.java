package com.cherry.rabbitmqstudy.basicapi.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-06-14 19:24
 */
public class Producer4Direct {
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

        //4.生产者只需要交换机的名字和routingKey就可以完成投递消息
        String exchangeName = "test_direct_exchange";
        String routingKey = "direct001";

        //5.投递消息
        String msg = "the exchange type is direct from rabbitMQ!";
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());

        //6.释放资源
        channel.close();
        connection.close();

    }
}
