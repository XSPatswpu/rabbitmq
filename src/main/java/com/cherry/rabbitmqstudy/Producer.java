package com.cherry.rabbitmqstudy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Auther: cherry
 * @Date: 2019-06-13 21:59
 */
public class Producer {
    /**
     * 生产者生产消息并发送给对应的交换机
     *
     * 1.创建连接工厂ConnectionFactory
     * 2.获取rabbitMQ的连接connection
     * 3.通过连接开辟通道channel
     * 4.通过channel发送数据
     * 5.释放资源
     */
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂，并配置属性
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机IP
        connectionFactory.setHost("172.20.10.3");
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/");

        //2.获取连接
        Connection connection = connectionFactory.newConnection();

        //3.通过连接开辟通道
        Channel channel = connection.createChannel();

        //4.通过channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = i + " hello rabbitMQ!";
            //注意由于此时没有指定交换机，那么采用默认的交换机即AMQP-default：
            //The default exchange is implicitly bound to every queue, with a routing key equal to the queue name.
            //只要routingKey与队列的名字相同那么该默认的交换机就与该队列绑定，因此消息即可传递到该队列中
            channel.basicPublish("","cherry001",null,msg.getBytes());
        }

        //5.释放资源
        channel.close();
        connection.close();

    }
}
