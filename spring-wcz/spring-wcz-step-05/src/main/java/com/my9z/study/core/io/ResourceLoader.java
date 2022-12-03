package com.my9z.study.core.io;

/**
 * @description: 资源加载器
 * @author: wczy9
 * @createTime: 2022-12-03  15:26
 */
public interface ResourceLoader {

    /**
     * 从类路径加载资源的前缀
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 获取资源处理的方式
     * @param location 资源所处的位置
     * @return Resource
     */
    Resource getResource(String location);

}