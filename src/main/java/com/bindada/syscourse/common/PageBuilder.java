package com.bindada.syscourse.common;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @desc 分页工具
 */
public class PageBuilder {

    private static <S, T> BasePageList<T> copy(Page<S> page) {
        BasePageList<T> result = new BasePageList<>();
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setCurrent(page.getCurrent());
        return result;
    }

    /**
     * 数据  转化
     *
     * @param page
     * @param list
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> BasePageList<T> copyAndConvert(Page<S> page, List<T> list) {
        BasePageList<T> result = copy(page);
        result.setRecords(list);
        return result;
    }

    /**
     * 数据转化可自定义方法转发
     *
     * @param page
     * @param function
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> BasePageList<T> copyAndConvert(Page<S> page, Function<S, T> function) {
        BasePageList<T> result = copy(page);
        result.setRecords(page.getRecords().stream().map(v -> function.apply(v)).collect(Collectors.toList()));
        return result;
    }

    /**
     * 数据转化 自定义类型
     *
     * @param page
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> BasePageList<T> copyAndConvert(Page page, Class<T> targetClass) {
        BasePageList<T> result = copy(page);
        result.setRecords(ObjectUtil.sourceList2TargetList(page.getRecords(), targetClass));
        return result;
    }
}
