package com.lg.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.service.StockReleaseRecordService;
import com.lg.entity.StockReleaseRecord;
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
 * @since 2021-04-26
 */

@Slf4j
@Api(tags = "")
@Controller
@RequestMapping("/lg/stockReleaseRecord")
public class StockReleaseRecordController {

    @Resource
    public StockReleaseRecordService stockReleaseRecordService;

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public R save(@RequestBody StockReleaseRecord stockReleaseRecord) {
        stockReleaseRecordService.save(stockReleaseRecord);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "根据id删除")
    @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        stockReleaseRecordService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<StockReleaseRecord>> list(@RequestBody StockReleaseRecord stockReleaseRecord) {
        List<StockReleaseRecord> stockReleaseRecordList = stockReleaseRecordService.list(new QueryWrapper<>(stockReleaseRecord));
        return R.ok(stockReleaseRecordList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<StockReleaseRecord>> list(@RequestParam Integer pageNum, @RequestParam Integer limit) {
        IPage<StockReleaseRecord> page = stockReleaseRecordService.page(pageNum, limit);
        return R.ok(page);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/get/{id}")
    public R<StockReleaseRecord> get(@PathVariable("id") Long id) {
        StockReleaseRecord stockReleaseRecord = stockReleaseRecordService.getById(id);
        return R.ok(stockReleaseRecord);
    }

    @ApiOperation(value = "根据id修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody StockReleaseRecord stockReleaseRecord) {
        stockReleaseRecordService.updateById(stockReleaseRecord);
        return R.ok("更新成功");
    }


}
