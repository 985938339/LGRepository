package com.lg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.config.StockSender;
import com.lg.entity.PmsProduct;
import com.lg.dao.PmsProductMapper;
import com.lg.entity.StockReleaseRecord;
import com.lg.exception.StockException;
import com.lg.service.PmsProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.service.StockReleaseRecordService;
import com.lg.utils.PageUtils;
import com.lg.vo.LockStockVo;
import com.lg.vo.StockItemVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {
    @Resource
    private StockReleaseRecordService stockReleaseRecordService;
    @Resource
    private StockSender stockSender;

    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    @Override
    public IPage<PmsProduct> page(Integer pageNum, Integer limit) {

        QueryWrapper<PmsProduct> queryWrapper = new QueryWrapper<>();

        return page(PageUtils.getPage(pageNum, limit), queryWrapper);

    }

    /**
     * 详情
     *
     * @param ids
     * @return
     */
    @Override
    public List<PmsProduct> info(List<Long> ids) {

        return this.baseMapper.selectBatchIds(ids);
    }

    /**
     * 新增
     *
     * @param param {table.comment!}对象
     * @return
     */
    @Override
    public void add(PmsProduct param) {
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());
        save(param);
    }

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public void modify(PmsProduct param) {

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void LockStock(LockStockVo lockStockVo) throws StockException {
        for (StockItemVo stockItemVo : lockStockVo.getStockItemVoList()) {
            if (this.baseMapper.lockStock(stockItemVo.getProductId(), stockItemVo.getCount()) != 1) {
                throw new StockException("库存不足");
            }
        }
        //锁定库存后，等1分钟检查订单有没有创建好，没有创建好的话，直接释放库存
        stockSender.send(lockStockVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void releaseStock(LockStockVo lockStockVo) {
        StockReleaseRecord record = new StockReleaseRecord();
        record.setOrderSn(lockStockVo.getOrderSn());
        //使用事务保证记录表和释放库存操作同时成功或者失败
        //todo 利用orderId作为mysql的唯一索引，保证消息的幂等性，就不会一直重复消费
        // （当然也可以使用redis来保证消息的幂等性，不过我认为使用mysql的可靠性更高，所以在这里使用了mysql）
        stockReleaseRecordService.add(record);
        for (StockItemVo stockItemVo : lockStockVo.getStockItemVoList()) {
            this.baseMapper.releaseStock(stockItemVo.getProductId(), stockItemVo.getCount());
        }

    }
}
