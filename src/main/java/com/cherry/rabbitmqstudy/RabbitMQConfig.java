package com.cherry.rabbitmqstudy;

import com.cherry.rabbitmqstudy.adapter.MessageDelegate;
import com.cherry.rabbitmqstudy.convert.ImageMessageConverter;
import com.cherry.rabbitmqstudy.convert.PDFMessageConverter;
import com.cherry.rabbitmqstudy.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
        cachingConnectionFactory.setAddresses("192.168.0.103:5672");
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
        注意消息的消费流程：
        先走TextMessageConvert进行消息的convert，然后再走MessageDelegate
        //1.适配器方式一
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //修改默认的方法名为自定义的方法名称
        adapter.setDefaultListenerMethod("consumerMessage");
        //定义消息的转换Convert
        adapter.setMessageConverter(new TextMessageConverter());
        //消费者开始监听消息
        container.setMessageListener(adapter);
        */


        /*
        //2.适配器方式二：队列名称和转换器的方法名进行一一匹配
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String,String> queueToMethod = new HashMap<>();
        queueToMethod.put("queue01","consumerMessage01");
        queueToMethod.put("queue02","consumerMessage02");
        adapter.setQueueOrTagToMethodName(queueToMethod);
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);
        */


        /*
        //1.1 Jackson2JsonMessageConverter 支持json格式的转换器
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("json2Message");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);
        //消息消费流程 --> Jackson2JsonMessageConverter --> MessageDelegate
        */


        /*
        //1.2 Jackson2JsonMessageConverter && DefaultJackson2JavaTypeMapper 支持Java对象的格式转换
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("message2Java");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //springboot升级2.0之后，不在信任所有package，需要额外进行添加需要信任的包名称
        defaultJackson2JavaTypeMapper.addTrustedPackages("com.cherry.rabbitmqstudy.entity");
        jackson2JsonMessageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);
        */


        /*
        //1.3 支持对个Java对象的映射
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("message2Java");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //springboot升级2.0之后，不在信任所有package，需要额外进行添加需要信任的包名称
        defaultJackson2JavaTypeMapper.addTrustedPackages("com.cherry.rabbitmqstudy.entity");

        Map<String,Class<?>> mapper = new HashMap<>();
        mapper.put("Order",com.cherry.rabbitmqstudy.entity.Order.class);
        mapper.put("Product",com.cherry.rabbitmqstudy.entity.Product.class);
        defaultJackson2JavaTypeMapper.setIdClassMapping(mapper);

        jackson2JsonMessageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);
        */

        //1.4 扩展转换器
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("message2Java");
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textMessageConverter = new TextMessageConverter();
        converter.addDelegate("text",textMessageConverter);
        converter.addDelegate("html/text",textMessageConverter);
        converter.addDelegate("xml/text",textMessageConverter);
        converter.addDelegate("text/plain",textMessageConverter);


        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json",jackson2JsonMessageConverter);
        converter.addDelegate("application/json",jackson2JsonMessageConverter);

        ImageMessageConverter imageMessageConverter = new ImageMessageConverter();
        converter.addDelegate("application/image",imageMessageConverter);
        converter.addDelegate("image",imageMessageConverter);

        PDFMessageConverter pdfMessageConverter = new PDFMessageConverter();
        converter.addDelegate("application/pdf",pdfMessageConverter);
        converter.addDelegate("pdf",pdfMessageConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);
        return container;
    }
}
