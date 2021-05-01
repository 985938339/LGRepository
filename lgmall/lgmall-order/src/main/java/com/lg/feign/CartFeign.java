package com.lg.feign;


import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "lgmall-cart")
public interface CartFeign {
    @GetMapping("/cart/cmsCartItem/removeItems")
    R removeItems(List<Long> ids);
}
//todo feign的阻塞回调和异常回调