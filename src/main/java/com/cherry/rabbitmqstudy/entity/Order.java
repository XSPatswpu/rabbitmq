package com.cherry.rabbitmqstudy.entity;

import lombok.Data;

/**
 * @Auther: cherry
 * @Date: 2019-07-15 10:36
 */
@Data
public class Order {
    private String id;
    private String name;
    private String content;

    public Order() {
    }

    public Order(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

}
