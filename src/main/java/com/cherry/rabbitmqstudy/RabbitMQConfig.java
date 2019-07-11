package com.cherry.rabbitmqstudy;

import com.cherry.rabbitmqstudy.adapter.MessageDelegate;
import com.cherry.rabbitmqstudy.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: cherry
 * @Date: 2019-07-10 09:28
 */
@Configuration
@ComponentScan("com.cherry.rabbitmqstudy")
public class RabbitMQConfig {

    //注意方法名称默认是bean的name
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses("192.168.0.101:5672");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setVirtualHost("/");
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public TopicExchange exchange01(){
        return new TopicExchange("topic_exchange01",true,false);
    }

    @Bean
    public Queue queue01(){
        return new Queue("queue01",true,false,false);
    }

    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(exchange01()).with("spring.#");
    }


    @Bean
    public TopicExchange exchange02(){
        return new TopicExchange("topic_exchange02",true,false);
    }

    @Bean
    public Queue queue02(){
        return new Queue("queue02",true,false,false);
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(exchange01()).with("rabbit.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }


    /*
    从MessageListenerContainer的API中可得出，该类主要充当的角色是消费者。
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //设置监听的队列
        container.setQueues(queue01(),queue02());
        //设置当前消费者数量
        container.setConcurrentConsumers(1);
        //设置最大消费者数量
        container.setMaxConcurrentConsumers(5);
        //设置是否重回队列
        container.setDefaultRequeueRejected(false);
        //设置签收模式
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置消费者的标签生成策略
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID());

        /*
        container.setMessageListener((message) -> {
            String msg = new String(message.getBody());
            System.out.println("----消费者：" + msg);
        });
        */

        /*
        //1.适配器方式一
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //修改默认的方法名为自定义的方法名称
        adapter.setDefaultListenerMethod("consumerMessage");
        //定义消息的转换Convert
        adapter.setMessageConverter(new TextMessageConverter());
        //消费者开始监听消息
        container.setMessageListener(adapter);
        */

        //2.适配器方式二：队列名称和方法名称也可以一一的匹配
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String,String> queueToMethod = new HashMap<>();
        queueToMethod.put("queue01","consumerMessage01");
        queueToMethod.put("queue02","consumerMessage02");
        adapter.setQueueOrTagToMethodName(queueToMethod);
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);
        return container;
    }
}
