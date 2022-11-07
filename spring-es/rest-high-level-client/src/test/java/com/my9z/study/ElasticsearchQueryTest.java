package com.my9z.study;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;


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
        printLog(searchResponse);
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
        printLog(searchResponse);
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
        printLog(searchResponse);
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
        printLog(searchResponse);
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
        String[] includeFields = new String[]{"name", "age"};
        //查询返回排除的字段
        String[] excludeFields = new String[]{""};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //指定查询的index和source
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        printLog(searchResponse);
    }


    @Test
    @SneakyThrows
    public void boolQueryTest() {
        //创建bool请求对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //构建bool查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "zhangsan"));
        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "女"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", "40"));
        //指定查询条件：bool查询
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        printLog(searchResponse);
    }

    @Test
    @SneakyThrows
    public void fuzzySearchTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：模糊查询
        searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name", "zhangsan").fuzziness(Fuzziness.ONE));
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest().indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //打印日志
        printLog(searchResponse);
    }

    @Test
    @SneakyThrows
    public void highlightSearchTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定查询条件：term查询
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "zhangsan"));
        //设置高亮查询对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color = 'red'>") //标签前缀
                .postTags("</font>") //标签后缀
                .field("name"); //高亮字段
        //设置请求体
        searchSourceBuilder.highlighter(highlightBuilder);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest().indices("student").source(searchSourceBuilder);
        //客户端发送请求 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //打印日志
        printLog(searchResponse);
    }

    @Test
    @SneakyThrows
    public void aggregateSearchTest() {
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定聚合字段和聚合方式  age字段的最大值 返回字段名为maxAge
        searchSourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age"));
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest().indices("student").source(searchSourceBuilder);
        //客户端发送轻松 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //打印日志
        printLog(searchResponse);
    }

    @Test
    @SneakyThrows
    public void aggregateGroupSearchTest(){
        //构建查询请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //指定分组条件 按照age来分组 分组名为age_group
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("age_group").field("age");
        //每一组求和age的值 对应字段名为sum_age
        termsAggregationBuilder.subAggregation(AggregationBuilders.sum("sum_age").field("age"));
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        //创建查询请求对象
        SearchRequest searchRequest = new SearchRequest().indices("student").source(searchSourceBuilder);
        //客户端发送轻松 获取响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //打印日志
        printLog(searchResponse);
    }

    private void printLog(SearchResponse searchResponse) {
        SearchHits hits = searchResponse.getHits();
        log.info("took:{}", searchResponse.getTook());
        log.info("timeout:{}", searchResponse.isTimedOut());
        log.info("total:{}", hits.getTotalHits());
        log.info("maxSource:{}", hits.getMaxScore());
        if (Objects.nonNull(searchResponse.getAggregations())) {
            log.info("aggregation:{}", JSONUtil.toJsonStr(searchResponse.getAggregations()));
        }
        for (SearchHit hit : hits) {
            log.info("hit:{}", hit.getSourceAsString());
            if (CollUtil.isNotEmpty(hit.getHighlightFields())) {
                log.info("highlightFields:{}", hit.getHighlightFields().toString());
            }
        }
    }
}
