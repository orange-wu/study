package com.my9z.study.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
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
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: RestHighLevelClient document常用API封装
 * @author: kim
 * @createTime: 2022-11-07  17:19
 */
@Component
@Slf4j
public class EsDocumentUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 向指定index中添加文档数据
     *
     * @param indexName  索引名词
     * @param docData    文档数据
     * @param idFunction id字段
     * @param <T>        文档类型
     * @param <R>        id字段类型
     * @return 是否添加成功
     */
    public <T extends Serializable, R> Boolean insert(@NonNull String indexName, @NonNull T docData, @NonNull Function<T, R> idFunction) {
        //保证id字段的值不为空
        R id = idFunction.apply(docData);
        if (Objects.isNull(id)) {
            log.warn("insert fail,because id is null,index:{},docData:{},idFunction:{}",
                    indexName, JSONUtil.toJsonStr(docData), idFunction);
        }
        //创建新增doc的请求对象
        IndexRequest indexRequest = new IndexRequest()
                //索引
                .index(indexName)
                //json格式的数据
                .source(JSONUtil.toJsonStr(docData), XContentType.JSON)
                //指定id
                .id(String.valueOf(id))
                //设置为true只做新增：若id已存在不做修改
                .create(true);
        //客户端发送请求 获取请求对象
        IndexResponse indexResponse;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("insertDoc fail:ES请求超时或者服务器无响应", e);
            return false;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                //如果 opType 设置为 create 并且已经存在具有相同索引和 id 的文档 或者 存在版本冲突（这里没有设置版本，只能是id重复）
                log.warn("insertDoc fail:文档id重复,indexName:{},docData:{},idFunction:{},id:{}",
                        indexName, JSONUtil.toJsonStr(docData), idFunction, id);
            } else {
                log.warn("insertDoc fail:新增文档失败,indexName:{},doc:{}", indexName, JSONUtil.toJsonStr(docData), e);
            }
            return false;
        }
        boolean result = DocWriteResponse.Result.CREATED == indexResponse.getResult();
        log.info(result ? "insertDoc request success,indexName:{},doc:{},id:{}"
                        : "insertDoc request fail,indexName:{},doc:{},id:{}",
                indexName, JSONUtil.toJsonStr(docData), id);
        return result;
    }

    /**
     * 通过id在指定index中获取document
     *
     * @param indexName 索引名
     * @param id        id值
     * @param clazz     返回类型
     * @param <T>       文档类型
     * @return 查找到的document实体
     */
    public <T extends Serializable> T getDocById(@NonNull String indexName, @NonNull String id, @NonNull Class<T> clazz) {
        //指定index id构建请求对象
        GetRequest getRequest = new GetRequest().index(indexName).id(id);
        GetResponse getResponse;
        try {
            //客户端发送请求 获取响应
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("getDocById fail:ES请求超时或者服务器无响应", e);
            return null;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                //索引不存在
                log.warn("getDocById fail:索引不存在 indexName:{}", indexName, e);
            } else {
                log.warn("getDocById fail:查询失败 indexName:{},id:{}", indexName, id, e);
            }
            return null;
        }
        return JSONUtil.toBean(getResponse.getSourceAsString(), clazz);
    }

    /**
     * 查询指定index中某个id的document是否存在
     *
     * @param indexName 索引名
     * @param id        id值
     * @return index中某个id的数据是否存在
     */
    public Boolean existsDoc(@NonNull String indexName, @NonNull String id) {
        //指定index id构建请求对象
        GetRequest getRequest = new GetRequest().index(indexName).id(id);
        boolean result;
        try {
            //客户端发送请求 获取响应
            result = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("existsDoc fail:ES请求超时或者服务器无响应", e);
            return null;
        }
        return result;
    }

    /**
     * 删除指定index中id值的数据
     *
     * @param indexName 索引名
     * @param id        id值
     * @return 是否删除成功
     */
    public Boolean deleteDoc(@NonNull String indexName, @NonNull String id) {
        //指定index id构建请求对象
        DeleteRequest deleteRequest = new DeleteRequest().index(indexName).id(id);
        DeleteResponse deleteResponse;
        try {
            //客户端发送请求 获取响应
            deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("deleteDoc fail:ES请求超时或者服务器无响应", e);
            return false;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                //索引不存在
                log.warn("deleteDoc fail:索引不存在 indexName:{}", indexName, e);
            } else {
                log.warn("deleteDoc fail:删除失败 indexName:{},id:{}", indexName, id, e);
            }
            return null;
        }
        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            log.warn("deleteDoc fail:数据不存在 indexName:{},id:{}", indexName, id);
            return false;
        }
        boolean result = DocWriteResponse.Result.DELETED == deleteResponse.getResult();
        log.info(result ? "deleteDoc request success indexName:{},id:{}"
                        : "deleteDoc request fail indexName:{},id:{}",
                indexName, id);
        return result;
    }

    /**
     * 修改指定index id的数据
     *
     * @param indexName 索引名
     * @param id        id值
     * @param updateDoc 需要修改的内容
     * @param <T>       返回的类型
     * @param clazz     返回对象的class
     * @return 修改后的数据
     */
    public <T extends Serializable> T updateDocById(@NonNull String indexName, @NonNull String id, @NonNull Object updateDoc, @NonNull Class<T> clazz) {
        if (!existsDoc(indexName, id)) {
            //指定数据不存在没有修改一说
            log.info("updateDocById error data:数据不存在 indexName:{} id:{}", indexName, id);
            return null;
        }
        //指定index id 以及修改的docData
        UpdateRequest updateRequest = new UpdateRequest()
                .index(indexName)
                .id(id)
                .doc(JSONUtil.toJsonStr(updateDoc), XContentType.JSON)
                .fetchSource(true)//启用数据检查 返回修改后的对象
                .docAsUpsert(true);//只能做修改操作（数据不存在也不会新增）
        //客户端发送请求 获取响应
        UpdateResponse updateResponse;
        try {
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("updateDocById fail:ES请求超时或者服务器无响应", e);
            return null;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                //索引不存在
                log.warn("updateDocById fail:索引不存在 indexName:{}", indexName, e);
            } else {
                log.warn("updateDocById fail:修改失败 indexName:{},id:{}", indexName, id, e);
            }
            return null;
        }
        //更新后的数据（fetchSource需要设置为true）
        GetResult getResult = updateResponse.getGetResult();
        //获取更新成功之后的数据
        T updateResult = JSONUtil.toBean(getResult.sourceAsString(), clazz);
        log.info("updateDocById request success indexName:{},id:{},updateData:{}", indexName, id, updateResult);
        return updateResult;
    }

    /**
     * 批量添加文档数据 (只会做新增操作,对于id已存在的数据不做修改)
     *
     * @param dataCollection 文档数据集合
     * @param indexName      索引名
     * @param idFunction     获取id的方法
     * @param <T>            文档类型
     * @param <R>            id字段类型
     * @return 请求是否成功
     */
    public <T extends Serializable, R> Boolean insertBatch(Collection<T> dataCollection, @NonNull String indexName, @NonNull Function<T, R> idFunction) {
        if (CollUtil.isEmpty(dataCollection)) {
            //批量插入的数据为空 直接返回true
            log.info("insertBatch success because dataCollection is empty");
            return true;
        }
        //过滤Id值为空的数据
        List<T> insertDataList = dataCollection.stream()
                .filter(data -> Objects.nonNull(idFunction.apply(data)))
                .collect(Collectors.toList());
        log.info("insertBatch 过滤id空值之后insertDataList:{}", JSONUtil.toJsonStr(insertDataList));
        if (CollUtil.isEmpty(insertDataList)) {
            //每条数据指定的id字段为空 直接返回false
            log.info("insertBatch fail because dataCollection all id value is null ");
            return false;
        }
        //构建批量请求对象
        BulkRequest bulkRequest = new BulkRequest();
        insertDataList.forEach(data -> {
            IndexRequest indexRequest = new IndexRequest()
                    .index(indexName)
                    .id(String.valueOf(idFunction.apply(data)))
                    .create(true)
                    .source(JSONUtil.toJsonStr(data), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("insertBatch fail:ES请求超时或者服务器无响应", e);
            return false;
        }
        log.info("insertBatch request success indexName:{},idList:{}", indexName, dataCollection.stream().map(idFunction).collect(Collectors.toList()));
        return true;
    }
}
