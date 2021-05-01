package com.lg.feign;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.vo.LockStockVo;
import com.lg.vo.ProductVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("lgmall-product")
public interface ProductFeign {
    @GetMapping("/product/pmsProduct/getByIds")
    R<List<ProductVo>> get(@RequestBody List<Long> ids);

    @PostMapping("/product/pmsProduct/lockStock")
    R lockStock(@RequestBody LockStockVo lockStockVo);

    @PostMapping("/product/pmsProduct/lockStock")
    R releaseStock(@RequestBody LockStockVo lockStockVo);
}
