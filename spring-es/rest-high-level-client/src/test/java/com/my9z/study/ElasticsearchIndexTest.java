package com.my9z.study;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: RestHighLevelClient的索引操作API
 * @author: wczy9
 * @createTime: 2022-11-03  09:57
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchIndexTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @SneakyThrows
    @Test
    public void createIndexTest(){
        CreateIndexRequest wczy = new CreateIndexRequest("wczy");
        CreateIndexResponse createResponse = restHighLevelClient.indices().create(wczy, RequestOptions.DEFAULT);
        log.info("result:{}",createResponse.isAcknowledged());
        log.info("response:{}",createResponse);
    }

    @SneakyThrows
    @Test
    public void getIndexTest(){
        GetIndexRequest wczy = new GetIndexRequest("wczy");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(wczy, RequestOptions.DEFAULT);
        log.info("aliases:{}",getIndexResponse.getAliases());
        log.info("mappings:{}",getIndexResponse.getMappings());
        log.info("settings:{}",getIndexResponse.getSettings());
        log.info("getIndexResponse:{}", JSONUtil.toJsonStr(getIndexResponse));
    }
    @SneakyThrows
    @Test
    public void deleteIndexTest(){
        DeleteIndexRequest wczy = new DeleteIndexRequest("wczy");
        AcknowledgedResponse deleteResponse = restHighLevelClient.indices().delete(wczy, RequestOptions.DEFAULT);
        log.info("deleteResponse:{}",JSONUtil.toJsonStr(deleteResponse));
    }

}
