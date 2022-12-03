package com.my9z.study.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import com.my9z.study.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 默认bean工厂的实现类
 * 继承AbstractAutowireCapableBeanFactory类，实现AbstractBeanFactory类中的getBeanDefinition方法；
 * 实现BeanDefinition接口，实现registerBeanDefinition方法来注册BeanDefinition
 * @author: kim
 * @createTime: 2022-11-16  19:24
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null)
            throw new BeanException("No bean named '" + beanName + "' is defined");
        return beanDefinition;
    }


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}
