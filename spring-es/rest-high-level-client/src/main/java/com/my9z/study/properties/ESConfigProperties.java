package com.my9z.study.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 读取配置文件的es相关配置
 * @author: wczy9
 * @createTime: 2022-11-02  15:35
 */
@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class ESConfigProperties {

    /**
     * es集群节点地址集合
     */
    private List<Nodes> nodes = new ArrayList<>();

    /**
     * 连接超时时间
     */
    private Integer connectTimeout = 5000;

    /**
     * 连接处理超时时间
     */
    private Integer socketTimeout = 40000;

    /**
     * 获取连接超时时间
     */
    private Integer connectionRequestTimeout = 10000;

    /**
     * 最大路由连接数
     */
    private Integer maxConnectPerRoute = 100;

    /**
     * 最大连接数
     */
    private Integer maxConnectTotal = 100;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
