package com.lg.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductVo {
    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private String note;

    private Integer stock;

    private Date createTime;

    private Date updateTime;
}
