package com.cherry.rabbitmqstudy.adapter;

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
}
