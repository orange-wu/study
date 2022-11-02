package com.my9z.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @description: RestHighLevelClient学习项目启动类
 * @author: wczy9
 * @createTime: 2022-11-02  15:27
 */
@SpringBootApplication
@EnableConfigurationProperties
public class EsHighClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsHighClientApplication.class, args);
    }
}
