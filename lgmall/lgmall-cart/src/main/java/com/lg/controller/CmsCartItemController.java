package com.lg.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.lg.service.CmsCartItemService;
import com.lg.entity.CmsCartItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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
@RequestMapping("/cart/cmsCartItem")
public class CmsCartItemController {

    @Resource
    public CmsCartItemService cmsCartItemService;

    @ApiOperation(value = "加入购物车")
    @PostMapping("/save")
    public R<String> save(@RequestBody List<Long> ids) throws Throwable {
        cmsCartItemService.add(ids);
        return R.ok("新增成功");
    }
//    @ApiOperation(value = "加入购物车")
//    @PostMapping("/saveOne/{id}")
//    public R<String> saveOne(@PathVariable("id") Long productId) throws Throwable {
//        List<Long> list = new ArrayList<>(1);
//        list.add(productId);
//        cmsCartItemService.add(list);
//        return R.ok("新增成功");
//    }
    @ApiOperation(value = "从购物车移除")
    @PostMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        cmsCartItemService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<CmsCartItem>> list(@RequestBody CmsCartItem cmsCartItem) {
        List<CmsCartItem> cmsCartItemList = cmsCartItemService.list(new QueryWrapper<>(cmsCartItem));
        return R.ok(cmsCartItemList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<CmsCartItem>> list(@RequestParam Integer pageNum,@RequestParam Integer limit) throws Throwable {
        IPage<CmsCartItem> page = cmsCartItemService.page(pageNum,limit);
        return R.ok(page);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/getByIds")
    public R<List<CmsCartItem>> get(@RequestBody List<Long> ids) {
       List<CmsCartItem> list = cmsCartItemService.listByIds(ids);
        return R.ok(list);
    }

    @ApiOperation(value = "根据id修改")
    @PostMapping("/update")
    public R<String> update(@RequestBody CmsCartItem cmsCartItem) {
        cmsCartItemService.updateById(cmsCartItem);
        return R.ok("更新成功");
    }

    @ApiOperation(value = "从购物车批量移除指定项")
    @PostMapping("/removeItems")
    public R<String> removeItems(@RequestBody List<Long> productIds) throws Throwable {
        cmsCartItemService.removeItems(productIds);
        return R.ok("更新成功");
    }

}
