package com.my9z.study.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: RestHighLevelClient常用API封装
 * @author: kim
 * @createTime: 2022-11-07  11:40
 */
@Component
@Slf4j
public class ESWczyTemplate {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /* ===================index api===================  */

    /**
     * 同步创建es索引
     *
     * @param indexName   索引名
     * @param shardsNum   主分片数量
     * @param replicasNum 副本分片数量
     * @param mappingJson mapping设置 JSON格式
     * @return 索引是否创建成功
     */
    public boolean createIndex(@NonNull String indexName, Integer shardsNum, Integer replicasNum, String mappingJson) {
        //创建index请求对象，设置index名
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        //设置索引的主分片数量，副本数量
        //默认主分片数量
        int defaultShardsNum = 3;
        //默认副本分片数量
        int defaultReplicasNum = 1;
        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", Objects.isNull(shardsNum) ? defaultShardsNum : shardsNum)
                .put("index.number_of_replicas", Objects.isNull(replicasNum) ? defaultReplicasNum : replicasNum));
        //设置index的mapping数据模型
        if (StrUtil.isNotBlank(mappingJson) && JSONUtil.isTypeJSON(mappingJson)) {
            createIndexRequest.mapping(mappingJson, XContentType.JSON);
        }
        //客户端请求 获取请求对象
        CreateIndexResponse createIndexResponse;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("createIndex:ES请求超时或者服务器无响应,indexName:{}", indexName, e);
            return false;
        } catch (ElasticsearchException e) {
            log.error("createIndex:索引创建失败,indexName:{}", indexName, e);
            return false;
        }
        return Objects.nonNull(createIndexResponse) && createIndexResponse.isAcknowledged();
    }

}
