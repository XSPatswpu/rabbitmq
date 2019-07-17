package com.cherry.rabbitmqstudy;

import com.cherry.rabbitmqstudy.entity.Order;
import com.cherry.rabbitmqstudy.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: cherry
 * @Date: 2019-07-15 10:32
 */
public class TestJson2Message extends AppTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testJson2Message() throws JsonProcessingException {
        Order order = new Order();
        order.setId("001");
        order.setContent("订单内容");
        order.setName("订单名称");
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(order);
        System.out.println("json String: " + jsonStr);
        //注意：要设置MessageProperties的ContentType为application/json
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        Message message = new Message(jsonStr.getBytes(),properties);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message);
    }


    @Test
    public void testJava2Message() throws JsonProcessingException {
        Order order = new Order();
        order.setId("001");
        order.setContent("订单内容");
        order.setName("订单名称");
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(order);
        System.out.println("json String: " + jsonStr);
        //注意：要设置MessageProperties的ContentType为application/json
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.getHeaders().put("__TypeId__","com.cherry.rabbitmqstudy.entity.Order");
        Message message = new Message(jsonStr.getBytes(),properties);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message);
    }

    @Test
    public void testJava2MessageMore() throws JsonProcessingException {
        Order order = new Order();
        order.setId("001");
        order.setContent("订单内容");
        order.setName("订单名称");
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(order);
        System.out.println("json Order String: " + jsonStr);
        //注意：要设置MessageProperties的ContentType为application/json
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        properties.getHeaders().put("__TypeId__","Order");
        Message message = new Message(jsonStr.getBytes(),properties);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message);

        Product product = new Product("002","商品名称","商品描述");
        String jsonStr01 = mapper.writeValueAsString(product);
        System.out.println("json Product String: " + jsonStr01);
        //注意：要设置MessageProperties的ContentType为application/json
        MessageProperties properties01 = new MessageProperties();
        properties01.setContentType("application/json");
        properties01.getHeaders().put("__TypeId__","Product");
        Message message01 = new Message(jsonStr01.getBytes(),properties01);
        rabbitTemplate.convertAndSend("topic_exchange01","spring.abc",message01);
    }
}
