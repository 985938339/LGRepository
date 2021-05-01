package com.lg.dao;

import com.lg.entity.StockReleaseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuguo
 * @since 2021-04-26
 */
@Mapper
@Repository
public interface StockReleaseRecordMapper extends BaseMapper<StockReleaseRecord> {

}
