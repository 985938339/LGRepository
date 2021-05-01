package com.lg.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.service.OrderProductService;
import com.lg.entity.OrderProduct;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */

@Slf4j
@Api(tags = "")
@Controller
@RequestMapping("/order/orderProduct")
public class OrderProductController {

    @Resource
    public OrderProductService orderProductService;

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public R<String> save(@RequestBody OrderProduct orderProduct) {
        orderProductService.save(orderProduct);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        orderProductService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<OrderProduct>> list(@RequestBody OrderProduct orderProduct) {
        List<OrderProduct> orderProductList = orderProductService.list(new QueryWrapper<>(orderProduct));
        return R.ok(orderProductList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<OrderProduct>> list(@RequestParam Integer pageNum, @RequestParam Integer limit) {
        IPage<OrderProduct> page = orderProductService.page(pageNum, limit);
        return R.ok(page);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/get/{id}")
    public R<OrderProduct> get(@PathVariable("id") String id) {
        OrderProduct orderProduct = orderProductService.getById(id);
        return R.ok(orderProduct);
    }

    @ApiOperation(value = "根据id修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody OrderProduct orderProduct) {
        orderProductService.updateById(orderProduct);
        return R.ok("更新成功");
    }


}
