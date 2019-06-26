package com.cherry.rabbitmqstudy.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Auther: cherry
 * @Date: 2019-06-26 10:23
 */
public class Consumer4Fanout {

    /*
    fanout交换机模式

    该模式下，消息不经过路由，因此消息投递的效率较高。

    消费端不需要设置路由key，只需建立交换机与队列的绑定即可。
    生产者端也不需要指定路由key，只需指定交换机的名字即可。
    他会把消息投递到所有与交换机绑定的队列中。

     */


    public static void main(String[] args) throws Exception {
        //1.创建连接工厂，并配置属性
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机IP
        connectionFactory.setHost("192.168.0.102");
        connectionFactory.setPort(5672);
        //设置虚拟主机
        connectionFactory.setVirtualHost("/");
        //设置自动重连，以及重连时间
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        //2.获取连接
        Connection connection = connectionFactory.newConnection();

        //3.通过连接开辟通道
        Channel channel = connection.createChannel();

        //4.声明交换机以及队列
        String exchangeName = "test_fanout_exchange";
        String queueName = "test_fanout_queue";
        String exchangeType = "fanout";
        String routingKey = "";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //5.创建生产者并设置ACK
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费者端：" + msg);
        }
    }
}
