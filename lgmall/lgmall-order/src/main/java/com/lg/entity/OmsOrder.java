package com.lg.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

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
@TableName("oms_order")
public class OmsOrder extends Model<OmsOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //订单序列号
    @TableField("order_sn")
    private String orderSn;
    //会员id
    @TableField("member_id")
    private Long memberId;

    //支付价格
    @TableField("pay_price")
    private BigDecimal payPrice;

    //订单状态：0：未支付，1：已支付, 2：已完成，3：已关闭
    @TableField("state")
    private Integer state;

    //支付时间
    @TableField("pay_time")
    private Date payTime;

    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}