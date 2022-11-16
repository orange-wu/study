package com.my9z.study.beans.factory.config;

/**
 * @description: bean对象中的依赖对象
 * 在Spring源码中，BeanReference是一个接口
 * @author: wczy9
 * @createTime: 2022-11-16  23:05
 */
public class BeanReference {

    /**
     * bean依赖的对象名称
     */
    private final String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

}