package com.lg.feign;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lgmall-order")
public interface OrderFeign {
    @GetMapping("/order/omsOrder/exist")
    R<Boolean> exist(@RequestParam("orderSn") String orderSn);
}
