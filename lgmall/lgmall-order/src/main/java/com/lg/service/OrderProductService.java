package com.lg.service;

import com.lg.entity.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
public interface OrderProductService extends IService<OrderProduct> {

    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    IPage<OrderProduct> page(Integer pageNum, Integer limit);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    OrderProduct info(Long id);

    /**
     * 新增
     *
     * @param list 根据需要进行传值
     * @return
     */
    void add(List<OrderProduct> list);

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    void modify(OrderProduct param);

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */
    void remove(Long id);

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    void removes(List<Long> ids);
}
