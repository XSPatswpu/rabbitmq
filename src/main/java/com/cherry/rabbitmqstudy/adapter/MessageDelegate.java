package com.cherry.rabbitmqstudy.adapter;

import com.cherry.rabbitmqstudy.entity.Order;
import com.cherry.rabbitmqstudy.entity.Product;

import java.io.File;
import java.util.Map;

/**
 * @Auther: cherry
 * @Date: 2019-07-11 17:13
 */
public class MessageDelegate {
    public void handleMessage(byte[] messageBody){
        System.out.println("默认方法，消息内容：" + new String(messageBody));
    }
    /*
    public void consumerMessage(String messageBody){
        System.out.println("自定义的方法，消息内容：" + messageBody);
    }
    */
    public void consumerMessage01(String messageBody){
        System.out.println("自定义的方法01，消息内容：" + messageBody);
    }
    public void consumerMessage02(String messageBody){
        System.out.println("自定义的方法02，消息内容：" + messageBody);
    }

    public void message2Json(Map messageBody){
        System.out.println("message2Json方法，消息类容：" + messageBody);
    }

    public void message2Java(Order order){
        System.out.println("Order方法，消息内容：" + order.toString());
    }

    public void message2Java(Product product){
        System.out.println("Product方法，消息内容：" + product.toString());
    }
    public void message2Java(File file){
        System.out.println("文件转换方法，消息内容：" + file.getName());
    }

}
