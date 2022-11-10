package com.my9z.study;

/**
 * @description: Bean对象信息定义
 * @author: wczy9
 * @createTime: 2022-11-10  20:18
 */
public class BeanDefinition {

    private final Object bean;

    public BeanDefinition(Object bean){
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }

}