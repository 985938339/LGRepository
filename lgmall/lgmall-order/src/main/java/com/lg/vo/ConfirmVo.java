package com.lg.vo;


import java.math.BigDecimal;
import java.util.List;

public class ConfirmVo {
    private List<ProductVo> list;
    private BigDecimal totalPrice = BigDecimal.valueOf(0);
    private String token;

    public List<ProductVo> getList() {
        return list;
    }

    public void setList(List<ProductVo> list) {
        this.list = list;
    }

    public BigDecimal getTotalPrice() {
        for (ProductVo p : list) {
            totalPrice = totalPrice.add(p.getPrice());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
