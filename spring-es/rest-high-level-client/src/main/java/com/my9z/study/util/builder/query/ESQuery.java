package com.my9z.study.util.builder.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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

    /**
     * 当前页数
     */
    private final Integer currentPage;

    /**
     * 页的大小
     */
    private final Integer pageSize;

    public ESQuery(Builder builder) {
        this.searchSourceBuilder = builder.searchSourceBuilder;
        this.indexes = builder.indexes;
        this.currentPage = builder.currentPage;
        this.pageSize = builder.pageSize;
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
        //当前页数
        private Integer currentPage;
        //页的大小
        private Integer pageSize;
        //es的排序查询参数
        private final List<SortBuilder<?>> sortBuilders = ListUtil.list(false);
        //es的bool查询
        private final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //指定should查询中需要满足几个条件(bool查询中的参数:should查询和其他查询组合使用时不设置这个值会导致should查询失效)
        private Integer minimumShouldMatch;
        //查询条件builder
        private final MustBuilder mustBuilder = new MustBuilder();
        private final ShouldBuilder shouldBuilder = new ShouldBuilder();
        private final MustNotBuilder mustNotBuilder = new MustNotBuilder();
        private final FilterBuilder filterBuilder = new FilterBuilder();

        /**
         * build构造ESQuery
         */
        public ESQuery build() {
            //bool查询的属性:minimum_number_should_match
            if (Objects.nonNull(minimumShouldMatch)) {
                boolQueryBuilder.minimumShouldMatch(minimumShouldMatch);
            }
            //bool查询对象
            searchSourceBuilder.query(boolQueryBuilder);
            //统计命中次数
            searchSourceBuilder.trackTotalHits(trackTotalHits);
            //es分页参数计算
            if (Objects.nonNull(currentPage) && Objects.nonNull(pageSize)) {
                searchSourceBuilder.from((currentPage - 1) * pageSize);
                searchSourceBuilder.size(pageSize);
            }
            //排序builder
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
         * 相当于OR, 使用should连接的查询条件只需要满足其中一个
         * 当其它连接方式存在时, should将会失效, 需要通过设置minimum_should_match
         */
        public MustNotBuilder mustNot() {
            return mustNotBuilder;
        }

        /**
         * 相当于NOT, 使用must_not连接的查询条件需要不满足
         */
        public ShouldBuilder should() {
            return shouldBuilder;
        }

        /**
         * 等同于must, 不同点在于filter不会参与打分, 只做过滤 查询效率会比must高一点
         */
        public FilterBuilder filter() {
            return filterBuilder;
        }

        /**
         * 指定index
         */
        public Builder indexes(String indexName) {
            indexes.add(indexName);
            return this;
        }

        /**
         * 指定多个index
         */
        public Builder indexes(List<String> indexNameList) {
            CollUtil.addAll(indexes, indexNameList);
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
            this.currentPage = Math.max(currentPage, 1);
            this.pageSize = pageSize < 0 ? 10 : pageSize;
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
         * 解决当should查询和其他查询组合使用时失效的问题
         *
         * @param minimumShouldMatch 需要满足should查询中的几个条件
         */
        public Builder minimumShouldMatch(int minimumShouldMatch) {
            this.minimumShouldMatch = Math.max(minimumShouldMatch, 1);
            return this;
        }

        /**
         * 模版方法模式，子类通过重写buildQuery来定义不同的查询连接方式：must must_not should filter
         */
        public abstract class BoolQueryAbstractBuilder {

            protected abstract void buildQuery(QueryBuilder queryBuilder);

            /**
             * termQuery查询 完全匹配 字段类型建议为:keyword
             *
             * @param key   指定字段名
             * @param value 字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder termQuery(String key, Object value) {
                minimumShouldMatch = 1;
                buildQuery(QueryBuilders.termQuery(key, value));
                return this;
            }

            /**
             * termsQuery查询 多个精确匹配 精确匹配多个值其中一个即可 字段类型建议为:keyword
             *
             * @param key    指定字段名
             * @param values 多个字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder termsQuery(String key, List<Object> values) {
                buildQuery(QueryBuilders.termsQuery(key, values));
                return this;
            }

            /**
             * termQuery查询 模糊查询 查找前对查找的内容进行分词 字段类型建议为:text
             *
             * @param key   指定字段名
             * @param value 字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder matchQuery(String key, Object value) {
                buildQuery(QueryBuilders.matchQuery(key, value));
                return this;
            }

            /**
             * 范围查询 匹配闭合区间内的值 start和end同时为空则不会进行范围查询的操作 字段类型建议为:number date
             *
             * @param key   指定字段名
             * @param start 区间开始值
             * @param end   区间结束值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder rangeQuery(String key, Object start, Object end) {
                if (Objects.isNull(start) && Objects.isNull(end)) {
                    return this;
                }
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
                if (Objects.nonNull(start)) {
                    rangeQueryBuilder.gte(start);
                }
                if (Objects.nonNull(end)) {
                    rangeQueryBuilder.lte(end);
                }
                buildQuery(rangeQueryBuilder);
                return this;
            }

            /**
             * 大于等于 字段类型建议为:number date
             *
             * @param key   指定字段名
             * @param value 字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder gte(String key, Object value) {
                return rangeQuery(key, value, null);
            }

            /**
             * 大于等于 字段类型建议为:number date
             *
             * @param key   指定字段名
             * @param value 字段值
             * @return BoolQueryAbstractBuilder链式组装
             */
            public final BoolQueryAbstractBuilder lte(String key, Object value) {
                return rangeQuery(key, null, value);
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
            protected void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.must(queryBuilder);
            }
        }

        public class FilterBuilder extends BoolQueryAbstractBuilder {
            @Override
            protected void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.filter(queryBuilder);
            }
        }

        public class MustNotBuilder extends BoolQueryAbstractBuilder {
            @Override
            protected void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.mustNot(queryBuilder);
            }
        }

        public class ShouldBuilder extends BoolQueryAbstractBuilder {
            @Override
            protected void buildQuery(QueryBuilder queryBuilder) {
                boolQueryBuilder.should(queryBuilder);
            }
        }
    }
}
