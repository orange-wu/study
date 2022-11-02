package com.my9z.study.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.my9z.study.properties.ESConfigProperties;
import com.my9z.study.properties.Nodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @description: elasticsearch配置类
 * @author: wczy9
 * @createTime: 2022-11-02  15:31
 */
@Configuration
@Slf4j
public class ElasticsearchConfig {

    @Autowired
    private ESConfigProperties esConfigProperties;

    private RestHighLevelClient creatRestClient(ESConfigProperties esConfigProperties) {
        if (Objects.isNull(esConfigProperties) || CollUtil.isEmpty(esConfigProperties.getNodes())) {
            throw new RuntimeException("ES 节点未配置");
        }
        //得到es实例节点地址
        List<Nodes> esNodes = esConfigProperties.getNodes();
        //通过节点地址构建HttpHost数组
        HttpHost[] httpHosts = esNodes.stream()
                .map(esNode -> new HttpHost(esNode.getIp(), esNode.getPort()))
                .toArray(HttpHost[]::new);
        //构建连接builder对象
        RestClientBuilder builder = RestClient.builder(httpHosts);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        log.info("ES 客户端创建成功: {}", JSONUtil.toJsonStr(esConfigProperties.getNodes()));
        // TODO: 2022/11/2 其他参数设置
        return restHighLevelClient;

    }

}
