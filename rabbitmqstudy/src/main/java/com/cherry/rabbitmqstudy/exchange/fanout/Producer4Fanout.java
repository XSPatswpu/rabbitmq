package com.cherry.rabbitmqstudy.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-06-26 10:24
 */
public class Producer4Fanout {

    /*
    生产者投递消息时，只需要交换机的名字不需要路由键的名字
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

        //4.生产者只需要交换机的名字就可以完成投递消息，此时消息不经过路由。
        String exchangeName = "test_fanout_exchange";
        String routingKey = "user.select";

        //5.投递消息

        for (int i = 0; i < 10; i++) {
            String msg1 = "the message is " + i;
            channel.basicPublish(exchangeName,routingKey,null,msg1.getBytes());
        }

        //6.释放资源
        channel.close();
        connection.close();
    }

}
