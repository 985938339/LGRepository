package com.lg.dao;

import com.lg.entity.OmsOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Mapper
@Repository
public interface OmsOrderMapper extends BaseMapper<OmsOrder> {

}
