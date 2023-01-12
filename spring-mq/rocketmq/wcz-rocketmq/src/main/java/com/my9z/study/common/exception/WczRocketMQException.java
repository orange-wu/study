package com.my9z.study.common.exception;

import lombok.NoArgsConstructor;

/**
 * @description: 自封装RocketMQ相关异常
 * @author: kim
 * @createTime: 2023-01-11  17:19
 */
@NoArgsConstructor
public class WczRocketMQException extends RuntimeException {

    protected String code;
    protected String message;

    public WczRocketMQException(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
