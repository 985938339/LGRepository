package com.lg.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.exception.StockException;
import com.lg.service.OmsOrderService;
import com.lg.entity.OmsOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.vo.SubmitVo;
import com.lg.vo.cartVo;
import com.lg.vo.ConfirmVo;
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
@RestController
@RequestMapping("/order/omsOrder")
public class OmsOrderController {

    @Resource
    public OmsOrderService omsOrderService;

    @ApiOperation(value = "生成确认信息")
    @PostMapping("/confirm")
    public R<ConfirmVo> confirm(@RequestBody List<cartVo> list){
        ConfirmVo confirmVo = omsOrderService.confirm(list);
        return R.ok(confirmVo);
    }

    @ApiOperation(value = "提交订单")
    @PostMapping("/submit")
    public R<String> submit(@RequestBody SubmitVo submitVo){
        omsOrderService.submit(submitVo);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        omsOrderService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<OmsOrder>> list(@RequestBody OmsOrder omsOrder) {
        List<OmsOrder> omsOrderList = omsOrderService.list(new QueryWrapper<>(omsOrder));
        return R.ok(omsOrderList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<OmsOrder>> list(@RequestParam Integer pageNum, @RequestParam Integer limit) {
        IPage<OmsOrder> page = omsOrderService.page(pageNum, limit);
        return R.ok(page);
    }

    @ApiOperation(value = "订单详情")
    @GetMapping("/get/{orderSn}")
    public R<OmsOrder> get(@PathVariable("orderSn") String orderSn) {
        OmsOrder omsOrder = omsOrderService.info(orderSn);
        return R.ok(omsOrder);
    }

    @ApiOperation(value = "是否存在该订单")
    @GetMapping("/exist")
    public R<Boolean> exist(@RequestParam String orderSn) {
        Boolean aBoolean = omsOrderService.existOrder(orderSn);
        return R.ok(aBoolean);
    }

    @ApiOperation(value = "根据id修改订单")
    @PostMapping("/update")
    public R<String> update(@RequestBody OmsOrder omsOrder) {
        omsOrderService.updateById(omsOrder);
        return R.ok("更新成功");
    }


}
