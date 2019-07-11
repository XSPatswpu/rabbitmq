package com.cherry.rabbitmqstudy;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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


}
