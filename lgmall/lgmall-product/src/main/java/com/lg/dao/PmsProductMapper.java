package com.lg.dao;

import com.lg.entity.PmsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Mapper
@Repository
public interface PmsProductMapper extends BaseMapper<PmsProduct> {
    @Update("update pms_product set stock = stock-#{count} where id = #{productId} and stock>=#{count}")
    int lockStock(@Param("productId")Long productId, @Param("count")Integer count);

    @Update("update pms_product set stock=stock+#{count} where id=#{productId}")
    int releaseStock(@Param("productId")Long productId, @Param("count")Integer count);
}
