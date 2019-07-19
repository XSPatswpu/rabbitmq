package com.cherry.rabbitmq4spring.entity;

import lombok.Data;

/**
 * @Auther: cherry
 * @Date: 2019-07-15 10:40
 */
@Data
public class Product {
    private String id;
    private String name;
    private String description;

    public Product() {
    }

    public Product(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
