package com.my9z.study.util;

/**
 * @description: calss工具类
 * @author: wczy9
 * @createTime: 2022-12-03  14:36
 */
public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception ignore) {

        }
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }

}