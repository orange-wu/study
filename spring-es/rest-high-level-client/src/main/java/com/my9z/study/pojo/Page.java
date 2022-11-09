package com.my9z.study.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: es分页查询返回对象
 * @author: kim
 * @createTime: 2022-11-09  16:01
 */
@Data
@NoArgsConstructor
public class Page<T> implements Serializable {

    /**
     * 数据总数
     */
    private long totalCount;

    /**
     * 页的大小
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currentPage;

    /**
     * 当前页的数据
     */
    private List<T> data;

    public Page(List<T> data, long totalCount, int pageSize, int currentPage) {
        this.data = data;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = (int) Math.ceil((double) totalCount / (double) pageSize);
    }




}
