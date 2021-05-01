package com.lg.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.exception.StockException;
import com.lg.service.PmsProductService;
import com.lg.entity.PmsProduct;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.vo.LockStockVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/product/pmsProduct")
public class PmsProductController {

    @Resource
    public PmsProductService pmsProductService;

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public R<String> save(@RequestBody PmsProduct pmsProduct) {
        pmsProductService.add(pmsProduct);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        pmsProductService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<PmsProduct>> list(@RequestBody PmsProduct pmsProduct) {
        List<PmsProduct> pmsProductList = pmsProductService.list(new QueryWrapper<>(pmsProduct));
        return R.ok(pmsProductList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<PmsProduct>> list(@RequestParam Integer pageNum, @RequestParam Integer limit) {
        IPage<PmsProduct> page = pmsProductService.page(pageNum, limit);
        return R.ok(page);
    }

    @PostMapping("/lockStock")
    public R<String> lockStock(@RequestBody LockStockVo lockStockVo) throws StockException {
        pmsProductService.LockStock(lockStockVo);
        return R.ok("锁定成功");
    }

    @ApiOperation(value = "详情")
    @GetMapping("/getByIds")
    public R<List<PmsProduct>> get(@RequestBody List<Long> ids) {
        List<PmsProduct> pmsProducts = pmsProductService.info(ids);
        return R.ok(pmsProducts);
    }

    @ApiOperation(value = "根据id修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody PmsProduct pmsProduct) {
        pmsProductService.updateById(pmsProduct);
        return R.ok("更新成功");
    }


}
