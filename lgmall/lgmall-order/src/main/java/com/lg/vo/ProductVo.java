package com.lg.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo {
    private BigDecimal price;
    private Integer stock;
    private String name;
    private Long id;
}
