package com.bindada.syscourse.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  分页返回基类
 */
@Data
public class BasePageList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需要显示的数据集
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 每页显示数量
     */
    @ApiModelProperty(value = "没页大小")
    private long size;

    /**
     * 可显示的页数
     */
    @ApiModelProperty(value = "总页数")
    private long pages;

    /**
     * 当前页码，从1开始
     */
    @ApiModelProperty(value = "当前页")
    private long current;

}
