package com.my9z.study.pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 文档操作数据模型
 * @author: wczy9
 * @createTime: 2022-11-04  23:37
 */
@Data
@Builder
public class User implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;

}