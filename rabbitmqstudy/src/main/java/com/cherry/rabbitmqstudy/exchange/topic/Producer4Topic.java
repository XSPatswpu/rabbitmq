package com.cherry.rabbitmqstudy.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-06-25 21:08
 */
public class Producer4Topic {

    /*
    猜想：交换机为Topic模式时，生产者的路由规则定义具体的规则而不是模糊匹配
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

        //4.生产者只需要交换机的名字和routingKey就可以完成投递消息
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.select";
        String routingKey2 = "user.delete";
        String routingKey3 = "user.update.add";

        //5.投递消息
        String msg1 = "the routingKey is user.select";
        String msg2 = "the routingKey is user.delete";
        String msg3 = "the routingKey is user.update.add";
        channel.basicPublish(exchangeName,routingKey1,null,msg1.getBytes());
        channel.basicPublish(exchangeName,routingKey2,null,msg2.getBytes());
        channel.basicPublish(exchangeName,routingKey3,null,msg3.getBytes());

        //6.释放资源
        channel.close();
        connection.close();
    }
}
