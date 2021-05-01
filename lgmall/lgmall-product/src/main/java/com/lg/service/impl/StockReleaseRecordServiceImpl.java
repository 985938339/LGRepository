package com.lg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.entity.StockReleaseRecord;
import com.lg.dao.StockReleaseRecordMapper;
import com.lg.service.StockReleaseRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-26
 */
@Service
public class StockReleaseRecordServiceImpl extends ServiceImpl<StockReleaseRecordMapper, StockReleaseRecord> implements StockReleaseRecordService {


    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    @Override
    public IPage<StockReleaseRecord> page(Integer pageNum, Integer limit) {

        QueryWrapper<StockReleaseRecord> queryWrapper = new QueryWrapper<>();

        return page(PageUtils.getPage(pageNum, limit), queryWrapper);

    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public StockReleaseRecord info(Long id) {

        return getById(id);
    }

    /**
     * 新增
     *
     * @param record {table.comment!}对象
     * @return
     */
    @Override
    public void add(StockReleaseRecord record) {
        record.setCreateTime(new Date());
        save(record);
    }

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public void modify(StockReleaseRecord param) {

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
