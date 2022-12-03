package com.my9z.study.beans;

/**
 * @description: bean对象中的属性信息
 * @author: wczy9
 * @createTime: 2022-11-16  22:57
 */
public class PropertyValue {

    /**
     * 属性名
     */
    private final String name;

    /**
     * 属性值(定义为Object，如果是依赖对象，则传入BeanReference对象)
     */
    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

}