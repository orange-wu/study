package com.my9z.study.util.builder.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Objects;

/**
 * @description: ES查询构造类
 * @author: wczy9
 * @createTime: 2022-11-08  20:08
 */
@Getter
public class ESQuery {

    /**
     * 查询资源构造
     */
    private final SearchSourceBuilder searchSourceBuilder;

    /**
     * 查询的index集合
     */
    private final List<String> indexes;

    public ESQuery(Builder builder) {
        this.searchSourceBuilder = builder.searchSourceBuilder;
        this.indexes = builder.indexes;

    }

    public static Builder builder() {
        return new Builder();
    }

    //链式编程构造ESQuery
    @Accessors(fluent = true)
    public static class Builder {
        //es的searchBuilder
        private final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //请求的index
        private final List<String> indexes = ListUtil.list(false);
        //是否需要统计命中总数
        private boolean trackTotalHits = true;
        //es的分页参数 from
        private Integer from;
        //es的分页参数 size
        private Integer size;
        //es的排序查询参数
        private final List<SortBuilder<?>> sortBuilders = ListUtil.list(false);
        //es的bool查询
        private final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件mustBuilder
        private final MustBuilder mustBuilder = new MustBuilder();

        /**
         * build构造ESQuery
         */
        public ESQuery build() {
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.trackTotalHits(trackTotalHits);
            if (Objects.nonNull(from)) {
                searchSourceBuilder.from(from);
            }
            if (Objects.nonNull(size)) {
                searchSourceBuilder.size(size);
            }
            if (CollUtil.isNotEmpty(sortBuilders)) {
                searchSourceBuilder.sort(sortBuilders);
            }
            return new ESQuery(this);
        }

        /**
         * 相当于AND, 使用must连接的查询条件需要全部满足
         */
        public MustBuilder must() {
            return mustBuilder;
        }

        /**
         * 指定index
         */
        public Builder indexes(String indexName) {
            indexes.add(indexName);
            return this;
        }

        /**
         * 是否需要统计命中总数
         */
        public Builder trackTotalHits(boolean trackTotalHits) {
            this.trackTotalHits = trackTotalHits;
            return this;
        }

        /**
         * 分页查询参数
         *
         * @param currentPage 第几页 小于1时取1
         * @param pageSize    一页几行 小于0时取10
         */
        public Builder page(int currentPage, int pageSize) {
            pageSize = pageSize < 0 ? 10 : pageSize;
            currentPage = Math.max(currentPage, 1);
            from = (currentPage - 1) * pageSize;
            size = pageSize;
            return this;
        }

        /**
         * 排序查询参数
         *
         * @param sortField 排序字段
         * @param isDesc    是否倒序
         */
        public Builder sort(String sortField, boolean isDesc) {
            sortBuilders.add(SortBuilders.fieldSort(sortField).order(isDesc ? SortOrder.DESC : SortOrder.ASC));
            return this;
        }


        /**
         * 模版方法模式，子类通过重写buildQuery来定义不同的查询连接方式：must must_not should filter
         */
        public abstract class BoolQueryAbstractBuilder {
            public abstract void buildQuery(QueryBuilder queryBuilder);

            /**
             * termQuery查询 完全匹配 字段类型建议为:keyword
             *
             * @param key   指定字段名
             * @param value 字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder termQuery(String key, Object value) {
                buildQuery(QueryBuilders.termQuery(key, value));
                return this;
            }

            /**
             * 结束该级联的查询组装 返回上一级Builder
             */
            public final Builder done() {
                return Builder.this;
            }


        }

        public class MustBuilder extends BoolQueryAbstractBuilder {
            @Override
            public void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.must(queryBuilder);
            }
        }

        public class FilterBuilder extends BoolQueryAbstractBuilder {
            @Override
            public void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.filter(queryBuilder);
            }
        }

        public class MustNotBuilder extends BoolQueryAbstractBuilder {
            @Override
            public void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.mustNot(queryBuilder);
            }
        }

        public class ShouldBuilder extends BoolQueryAbstractBuilder {
            @Override
            public void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.should(queryBuilder);
            }
        }


    }
}
