package com.my9z.study.beans;

/**
 * @description: bean异常
 * @author: kim
 * @createTime: 2022-11-16  18:34
 */
public class BeansException extends RuntimeException{

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg,Throwable cause){
        super(msg, cause);
    }

}
