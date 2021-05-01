package com.lg.vo;

import lombok.Data;

import java.util.List;
@Data
public class SubmitVo {
    private List<ProductVo> productVoList;
    private String token;
}
