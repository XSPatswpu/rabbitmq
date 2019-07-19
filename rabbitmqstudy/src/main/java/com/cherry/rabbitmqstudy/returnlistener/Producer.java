package com.cherry.rabbitmqstudy.returnlistener;

import com.rabbitmq.client.*;

/**
 * @Auther: cherry
 * @Date: 2019-07-07 10:19
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.100");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.创建连接
        Connection connection = connectionFactory.newConnection();

        //3.创建渠道
        Channel channel = connection.createChannel();

        //4.根据交换机名称和routingKey发送消息
        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "error.save";

        String msg = "hello rabbitMQ return message!";
        channel.basicPublish(exchangeName,routingKeyError,true,null,msg.getBytes());

        //5.returnListener
//        channel.addReturnListener(new ReturnListener() {
//            @Override
//            public void handleReturn(int replyCode, String replyText, String exchange,
//                                     String routingKey, AMQP.BasicProperties properties,
//                                     byte[] body) throws IOException {
//                System.out.println("---------handle return---------");
//                System.out.println("replyCode: " + replyCode);
//                System.out.println("replyText: " + replyText);
//                System.out.println("exchange: " + exchange);
//                System.out.println("routingKey: " + routingKey);
//                System.out.println("properties: " + properties);
//                System.out.println("body: " + new String(body));
//            }
//        });
        channel.addReturnListener((replyCode,replyText,exchange,routingKey0,properties,body) -> {
            System.out.println("---------handle return---------");
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey0);
            System.out.println("properties: " + properties);
            System.out.println("body: " + new String(body));
        });
    }
}
