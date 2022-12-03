package com.my9z.study.beans.factory.support;

import com.my9z.study.core.io.DefaultResourceLoader;
import com.my9z.study.core.io.ResourceLoader;

/**
 * @description: 读取BeanDefinition的抽象实现
 * @author: wczy9
 * @createTime: 2022-12-03  15:39
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private final BeanDefinitionRegistry registry;

    private final ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}