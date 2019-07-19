package com.cherry.rabbitmqstudy.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cherry
 * @Date: 2019-06-26 15:15
 */
public class Producer {

    /*
    发送带有附加属性的消息
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

        Map<String,Object> header = new HashMap<>();
        header.put("myValue1","001");
        header.put("myValue2","002");
        header.put("myValue3","003");


        //创建properties
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder()
                .deliveryMode(1) //设置消息投递的模式
                .contentEncoding("UTF-8")
                .expiration("5000")
                .headers(header)
                .build();

        //4.通过channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = i + " hello rabbitMQ!";
            channel.basicPublish("","cherry001",props,msg.getBytes());
        }

        //5.释放资源
        channel.close();
        connection.close();
    }
}
