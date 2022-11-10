package com.my9z.study;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: bean工厂
 * @author: wczy9
 * @createTime: 2022-11-10  20:19
 */
public class BeanFactory {

    /**
     * 存储BeanDefinition对象信息的Map
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 获取bean
     *
     * @param name bean的名字
     * @return 对应的bean对象
     */
    public Object getBean(String name) {
        return beanDefinitionMap.get(name).getBean();
    }

    /**
     * bean对象注册
     *
     * @param name           bean的名字
     * @param beanDefinition BeanDefinition对象
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

}