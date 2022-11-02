package com.my9z.study.properties;

import lombok.Data;

/**
 * @description: es节点地址配置
 * @author: wczy9
 * @createTime: 2022-11-02  17:27
 */
@Data
public class Nodes {

    /**
     * ip
     */
    private String ip;

    /**
     * http通讯端口
     */
    private int port;
}
