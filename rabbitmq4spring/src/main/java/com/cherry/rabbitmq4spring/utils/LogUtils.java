package com.cherry.rabbitmq4spring.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LogUtils {

    // 一般的通用log
    public static final Logger COMMON = LogManager.getLogger("common");

    // 错误log
    public static final Logger ERROR = LogManager.getLogger("error");

}
