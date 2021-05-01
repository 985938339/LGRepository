package com.lg.dao;

import com.lg.entity.UmsMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员 Mapper 接口
 * </p>
 *
 * @author liuguo
 * @since 2021-04-15
 */
@Mapper
@Repository
public interface UmsMemberMapper extends BaseMapper<UmsMember> {

}
