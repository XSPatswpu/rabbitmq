package com.cherry.rabbitmq4spring;

import com.cherry.rabbitmq4spring.utils.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Rabbitmq4springApplication {

	public static void main(String[] args) {
		SpringApplication.run(Rabbitmq4springApplication.class, args);

		LogUtils.COMMON.debug("start finish!");

	}

}
