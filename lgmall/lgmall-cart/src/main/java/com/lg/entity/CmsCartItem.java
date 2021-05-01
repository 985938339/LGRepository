package com.lg.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Data
@TableName("cms_cart_item")
public class CmsCartItem extends Model<CmsCartItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //会员id
    @TableField("member_id")
    private Long memberId;

    //产品id
    @TableField("product_id")
    private Long productId;

    //产品名称
    @TableField("product_name")
    private String productName;

    //产品价格
    @TableField("price")
    private BigDecimal price;

    //产品数量
    @TableField("count")
    private Integer count;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}