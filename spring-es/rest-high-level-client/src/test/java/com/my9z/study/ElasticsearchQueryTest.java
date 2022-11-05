package com.my9z.study;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @description: RestHighLevelClient的文档操作API
 * @author: wczy9
 * @createTime: 2022-11-05  11:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchQueryTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    @SneakyThrows
    public void matchAllQueryTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：查询全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }

    @Test
    @SneakyThrows
    public void termQueryTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：term查询
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "zhangsan"));
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }

    @Test
    @SneakyThrows
    public void pageQueryTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：查询全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //分页查询，从第0条开始总共查询2条
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }

    @Test
    @SneakyThrows
    public void sortQueryTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：查询全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //指定排序字段以及升序或降序（默认降序）
        searchSourceBuilder.sort("age", SortOrder.DESC);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }

    @Test
    @SneakyThrows
    public void filterQueryTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：查询全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //查询字段过滤
        //查询返回包含的字段
        String[] includeFields = new String[]{"name","age"};
        //查询返回排除的字段
        String[] excludeFields = new String[]{""};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }

    @Test
    @SneakyThrows
    public void boolQueryTest() {
        //创建bool请求对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //构建bool查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("name","zhangsan"));
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex","女"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", "40"));
        //创建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        //
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("student").source(searchSourceBuilder);
        //
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
        }
    }
}