package com.cherry.rabbitmqstudy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Auther: cherry
 * @Date: 2019-07-10 10:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testAdmin(){
        rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange",false,false));
        rabbitAdmin.declareQueue(new Queue("test.direct.queue",false,false,false));
        rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE,
                "test.direct.exchange","direct",null));

        rabbitAdmin.declareExchange(new TopicExchange("test.topic.exchange",false,false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue",false,false,false));
        /*
        困惑：本以为在BindingBuilder中的链式编程中，可以直接声明队列和交换机然后进行绑定。但是通过测试发现必须要对交换机和队列进行单独声明。
         */
        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("test.topic.queue",false,false,false))
                        .to(new TopicExchange("test.topic.exchange",false,false))
                        .with("user.#"));

        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout.exchange",false,false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue",false,false,false));
        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.fanout.queue",false,false,false))
                .to(new FanoutExchange("test.fanout.exchange",false,false)));

        //清空队列
//        rabbitAdmin.purgeQueue("dlx.queue",false);
    }

}