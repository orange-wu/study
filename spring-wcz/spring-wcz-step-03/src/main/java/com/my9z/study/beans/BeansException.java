package com.my9z.study.beans;

/**
 * @description: bean异常
 * @author: wczy9
 * @createTime: 2022-11-10  22:38
 */
public class BeansException extends RuntimeException{

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg,Throwable cause){
        super(msg, cause);
    }

}