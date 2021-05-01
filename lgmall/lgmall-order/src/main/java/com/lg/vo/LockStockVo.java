package com.lg.vo;

import lombok.Data;

import java.util.List;

@Data
public class LockStockVo {
    private List<cartVo> stockItemVoList;
    private String orderSn;
}
