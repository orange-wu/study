package com.my9z.study.beans.factory;

import com.my9z.study.beans.BeansException;

/**
 * @description: 定义bean工厂接口
 * @author: wczy9
 * @createTime: 2022-11-10  22:33
 */
public interface BeanFactory {

    /**
     * 返回bean的实例对象
     *
     * @param name 要查找的bean的名称
     * @return 实例化的bean对象
     * @throws BeansException 不能实例化bean时抛出异常
     */
    Object getBean(String name) throws BeansException;

    /**
     * 返回bean的实例对象
     *
     * @param name 要查找的bean的名称
     * @param args 实例化需要的参数
     * @return 实例化的bean对象
     * @throws BeansException 不能实例化bean时抛出异常
     */
    Object getBean(String name, Object... args) throws BeansException;

}