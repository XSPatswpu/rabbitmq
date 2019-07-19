package com.cherry.rabbitmqstudy.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Auther: cherry
 * @Date: 2019-06-25 21:00
 */
public class Consumer4Topic {
    /*
    主题模式中，消费者的routingKey定义为模糊匹配

    注意模糊匹配的过程中，猜想可能routingKey的默认规则为xxx.xxx.xxx的形式。
    模糊匹配过程中，“*”表示一个单词的模糊匹配。“#”代表多个单词的模糊匹配，单词之间用“.”隔开

     */
    /**
     * 注意消费者与生产者的区别：
     * 消费者需要监听队列，因此在消费者端需要声明队列和交换机
     * 生产者只需要知道交换机的名字和routingKey就可以完成消息的传递
     *
     * 1.创建连接工厂ConnectionFactory
     * 2.获取rabbitMQ的连接connection
     * 3.通过连接开辟通道channel
     * 4.通过channel声明队列queue，声明exchange，并设置queue和exchange的绑定关系
     * 5.创建生产者进行消费
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
        String exchangeName = "test_topic_exchange";
        String queueName = "test_topic_queue";
        String exchangeType = "topic";
        String routingKey = "user.*";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        channel.queueBind(queueName,exchangeName,routingKey);

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
