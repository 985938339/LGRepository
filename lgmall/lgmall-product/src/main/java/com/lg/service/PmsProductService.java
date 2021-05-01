package com.lg.service;

import com.lg.entity.PmsProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.exception.StockException;
import com.lg.vo.LockStockVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
public interface PmsProductService extends IService<PmsProduct> {

    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    IPage<PmsProduct> page(Integer pageNum, Integer limit);

    /**
     * 详情
     *
     * @param ids
     * @return
     */
    List<PmsProduct> info(List<Long> ids);

    /**
     * 新增
     *
     * @param param 根据需要进行传值
     * @return
     */
    void add(PmsProduct param);

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    void modify(PmsProduct param);

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

    void LockStock(LockStockVo lockStockVo) throws StockException;

    void releaseStock(LockStockVo lockStockVo);
}
