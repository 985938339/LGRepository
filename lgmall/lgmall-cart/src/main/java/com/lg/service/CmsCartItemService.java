package com.lg.service;

import com.lg.entity.CmsCartItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
public interface CmsCartItemService extends IService<CmsCartItem> {

    /**
     * 分页列表
     *
     */
    IPage<CmsCartItem> page(Integer pageNum, Integer limit) throws Throwable;

    List<CmsCartItem> itemList() throws Throwable;

    /**
     * 详情
     *
     * @param id
     * @return
     */
    CmsCartItem info(Long id) throws Throwable;

    /**
     * 新增
     *
     * @param ids 根据需要进行传值
     * @return
     */
    void add(List<Long> ids) throws Throwable;

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    void modify(CmsCartItem param) throws Throwable;

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */
    void remove(Long id) throws Throwable;

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    void removes(List<Long> ids) throws Throwable;

    void removeItems(List<Long> productIds) throws Throwable;
}
