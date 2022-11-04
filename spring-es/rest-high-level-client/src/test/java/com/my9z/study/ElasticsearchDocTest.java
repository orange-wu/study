package com.my9z.study;

import cn.hutool.json.JSONUtil;
import com.my9z.study.pojo.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: es配置单元测试
 * @author: wczy9
 * @createTime: 2022-11-03  23:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchDocTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    @SneakyThrows
    public void createDocTest() {
        User wcz = User.builder().name("wcz").age(23).sex("boy").build();
        //创建新增文档请求对象
        IndexRequest indexRequest = new IndexRequest();
        //设置索引，id，文档数据，指定文档数据为json格式
        indexRequest.index("user")
                .id("1001")
                .source(JSONUtil.toJsonStr(wcz), XContentType.JSON);
        //客户端发送请求，获取响应
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("_index:{}", indexResponse.getIndex());
        log.info("_id:{}", indexResponse.getId());
        log.info("_result:{}", indexResponse.getResult());
        log.info("indexResponse:{}", JSONUtil.toJsonStr(indexResponse));
    }

    @Test
    @SneakyThrows
    public void updateDocTest() {
        //创建修改文档请求对象
        UpdateRequest updateRequest = new UpdateRequest();
        //设置索引，id，文档格式，修改的内容(有很多重载doc方法)
        updateRequest.index("user")
                .id("1001")
                .doc(XContentType.JSON, "age", 18);
        //客户端发送请求，获取响应
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("_index:{}", updateResponse.getIndex());
        log.info("_id:{}", updateResponse.getId());
        log.info("_result:{}", updateResponse.getResult());
        log.info("indexResponse:{}", JSONUtil.toJsonStr(updateResponse));
    }

    @Test
    @SneakyThrows
    public void getDocTest() {
        //创建查询文档请求对象
        GetRequest getRequest = new GetRequest();
        //指定查询的index和id
        getRequest.index("user")
                .id("1001");
        //客户端发送请求，获取响应
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        log.info("_index:{}", getResponse.getIndex());
        log.info("_type:{}", getResponse.getType());
        log.info("_id:{}", getResponse.getId());
        log.info("_source:{}", getResponse.getSource());
        log.info("getResponse:{}", getResponse);
    }

    @Test
    @SneakyThrows
    public void deleteDocTest() {
        //创建删除文档请求对象
        DeleteRequest deleteRequest = new DeleteRequest();
        //指定删除的index和id
        deleteRequest.index("user")
                .id("1001");
        //客户端发送请求 获取响应
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        log.info("_index:{}", deleteResponse.getIndex());
        log.info("_id:{}", deleteResponse.getId());
        log.info("_result:{}", deleteResponse.getResult());
        log.info("deleteResponse:{}", JSONUtil.toJsonStr(deleteResponse));
    }

    @Test
    @SneakyThrows
    public void bulkCreatTest() {
        //创建多个新增文档请求对象
        User wcz = User.builder().name("wcz").age(18).sex("boy").build();
        IndexRequest wczIndex = new IndexRequest()
                .index("user")
                .id("1001")
                .source(JSONUtil.toJsonStr(wcz), XContentType.JSON);
        User zxb = User.builder().name("zxb").age(18).sex("boy").build();
        IndexRequest zxbIndex = new IndexRequest()
                .index("user")
                .id("1002")
                .source(JSONUtil.toJsonStr(zxb), XContentType.JSON);
        User ly = User.builder().name("ly").age(18).sex("boy").build();
        IndexRequest lyIndex = new IndexRequest()
                .index("user")
                .id("1001")
                .source(JSONUtil.toJsonStr(ly), XContentType.JSON);
        //创建批量请求对象
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(wczIndex).add(zxbIndex).add(lyIndex);
        //客户端发送请求 获取响应
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("took:{}", bulkResponse.getTook());
        log.info("items:{}", JSONUtil.toJsonStr(bulkResponse.getItems()));
        log.info("bulkResponse:{}", JSONUtil.toJsonStr(bulkResponse));
    }
}
