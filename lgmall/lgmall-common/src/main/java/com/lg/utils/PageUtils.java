package com.lg.utils;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;


/**
 * 分页工具类
 */
public class PageUtils {
    public static <T> Page<T> getPage(Integer pageNum, Integer limit) {
        Page<T> page = new Page<>();
        if (pageNum != null && limit != null) {
            page.setCurrent(pageNum);
            page.setSize(limit);
        } else {
            page.setCurrent(0).setSize(15);
        }
        return page;
    }

}

