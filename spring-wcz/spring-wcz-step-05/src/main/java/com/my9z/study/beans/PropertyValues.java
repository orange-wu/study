package com.my9z.study.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 一个bean对象中的属性值集合
 * @author: wczy9
 * @createTime: 2022-11-16  23:01
 */
public class PropertyValues {

    /**
     * 一个bean对象中依赖的属性集合
     */
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.add(propertyValue);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : this.propertyValueList) {
            if (propertyValue.getName().equals(propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }

}