package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.core.io.Resource;
import com.my9z.study.core.io.ResourceLoader;

/**
 * @description: 读取BeanDefinition的抽象接口
 * @author: wczy9
 * @createTime: 2022-12-03  15:36
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;
}