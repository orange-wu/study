package com.my9z.study.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.my9z.study.properties.ESConfigProperties;
import com.my9z.study.properties.Nodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    /**
     * 装载RestHighLevelClient bean对象到spring容器中
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return creatRestClient(esConfigProperties);
    }

    /**
     * 根据es配置创建RestHighLevelClient
     *
     * @param esConfigProperties es配置
     * @return RestHighLevelClient
     */
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
        //认证配置
        if (StrUtil.isNotBlank(esConfigProperties.getUsername())) {
            BasicCredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esConfigProperties.getUsername(), esConfigProperties.getPassword()));
            builder.setHttpClientConfigCallback(httpClientBuild -> {
                httpClientBuild.setDefaultCredentialsProvider(provider);
                return httpClientBuild;
            });
        }
        //异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuild -> {
            requestConfigBuild.setConnectTimeout(esConfigProperties.getConnectTimeout());
            requestConfigBuild.setSocketTimeout(esConfigProperties.getSocketTimeout());
            requestConfigBuild.setConnectionRequestTimeout(esConfigProperties.getConnectionRequestTimeout());
            return requestConfigBuild;
        });
        //异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuild -> {
            httpClientBuild.setMaxConnTotal(esConfigProperties.getMaxConnectTotal());
            httpClientBuild.setMaxConnPerRoute(esConfigProperties.getMaxConnectPerRoute());
            return httpClientBuild;
        });
        //构建RestHighLevelClient
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        log.info("ES 客户端创建成功: {}", JSONUtil.toJsonStr(esConfigProperties.getNodes()));
        return restHighLevelClient;
    }

}
