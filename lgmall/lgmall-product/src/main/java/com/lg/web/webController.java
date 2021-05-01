package com.lg.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.entity.PmsProduct;
import com.lg.service.PmsProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class webController {
    @Resource
    private PmsProductService pmsProductService;
    @GetMapping({"/", "index.html"})
    public String toIndex(Map<String,Object> map) {
        IPage<PmsProduct> page = pmsProductService.page(0, 10);
        map.put("products",page.getRecords());
        return "index";
    }
}
