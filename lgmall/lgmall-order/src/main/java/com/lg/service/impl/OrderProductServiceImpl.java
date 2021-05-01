package com.lg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.entity.OrderProduct;
import com.lg.dao.OrderProductMapper;
import com.lg.service.OrderProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {


    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    @Override
    public IPage<OrderProduct> page(Integer pageNum, Integer limit) {

        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();

        return page(PageUtils.getPage(pageNum, limit), queryWrapper);

    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public OrderProduct info(Long id) {

        return getById(id);
    }

    /**
     * 新增
     *
     * @param list {table.comment!}对象
     * @return
     */
    @Override
    public void add(List<OrderProduct> list) {
        saveBatch(list);
    }

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public void modify(OrderProduct param) {

        updateById(param);
    }

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) {
        removeById(id);
    }

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    @Override
    public void removes(List<Long> ids) {

        removeByIds(ids);
    }

}
