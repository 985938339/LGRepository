package com.lg.service;

import com.lg.entity.OmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.exception.StockException;
import com.lg.vo.ProductVo;
import com.lg.vo.SubmitVo;
import com.lg.vo.cartVo;
import com.lg.vo.ConfirmVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
public interface OmsOrderService extends IService<OmsOrder> {

    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    IPage<OmsOrder> page(Integer pageNum, Integer limit);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    OmsOrder info(String orderSn);

    /**
     * 新增
     *
     * @param param 根据需要进行传值
     * @return
     */
    void add(OmsOrder param);

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    void modify(OmsOrder param);

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

    ConfirmVo confirm(List<cartVo> list) throws StockException;

    void submit(SubmitVo submitVo) throws Exception;

    void closeOrder(OmsOrder order);

    Boolean existOrder(String orderSn);
}
