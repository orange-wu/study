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
    private List<Nodes> nodes = new ArrayList<>();

    /**
     * 连接超时时间
     */
    private Integer connectTimeout;

    /**
     * 连接处理超时时间
     */
    private Integer socketTimeout;

    /**
     * 获取连接超时时间
     */
    private Integer connectionRequestTimeout;

    /**
     * 最大路由连接数
     */
    private Integer maxConnectPerRoute;

    /**
     * 最大连接数
     */
    private Integer maxConnectTotal;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
