package com.my9z.study.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: RestHighLevelClient index常用API封装
 * @author: kim
 * @createTime: 2022-11-07  11:40
 */
@Component
@Slf4j
public class ESIndexUtil {

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
    public Boolean createIndex(@NonNull String indexName, Integer shardsNum, Integer replicasNum, String mappingJson) {
        //创建新建index请求对象，设置index名
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
            log.error("createIndex fail:ES请求超时或者服务器无响应,indexName:{}", indexName, e);
            return false;
        } catch (ElasticsearchException e) {
            log.error("createIndex fail:索引创建失败,indexName:{},status:{}", indexName, e.status(), e);
            return false;
        }
        boolean result = Objects.nonNull(createIndexResponse) && createIndexResponse.isAcknowledged();
        log.info(result ? "createIndex request success,indexName:{}" : "createIndex request fail,indexName:{}", indexName);
        return result;
    }

    /**
     * 根据index的名词删除index
     *
     * @param indexName 索引名
     * @return 是否删除成功
     */
    public Boolean deleteIndex(@NonNull String indexName) {
        //创建删除index请求对象，设置index名
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        //客户端发起请求 获取返回对象
        AcknowledgedResponse deleteResponse;
        try {
            deleteResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("deleteIndex fail:ES请求超时或者服务器无响应,indexName:{}", indexName, e);
            return false;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                log.error("deleteIndex fail:此索引不存在,indexName:{}", indexName, e);
            } else {
                log.error("deleteIndex fail:索引删除失败,indexName:{},status:{}", indexName, e.status(), e);
            }
            return false;
        }
        boolean result = Objects.nonNull(deleteResponse) && deleteResponse.isAcknowledged();
        log.info(result ? "deleteIndex request success,indexName:{}" : "deleteIndex request fail,indexName:{}", indexName);
        return result;
    }

    /**
     * 查看index是否存在
     *
     * @param indexName 索引名
     * @return true代表存在 false代表不存在 null代表连接失败
     */
    public Boolean existsIndex(@NonNull String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists;
        try {
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("existsIndex fail:ES请求超时或者服务器无响应,indexName:{}", indexName, e);
            return null;
        }
        return exists;
    }

}
