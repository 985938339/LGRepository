package com.lg.controller;
import com.baomidou.mybatisplus.extension.api.R;
import com.lg.service.UmsMemberService;
import com.lg.entity.UmsMember;
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
 * 会员 前端控制器
 * </p>
 *
 * @author liuguo
 * @since 2021-04-15
 */

@Slf4j
@Api(tags = "会员")
@Controller
@RequestMapping("/admin/umsMember")
public class UmsMemberController {

    @Resource
    public UmsMemberService umsMemberService;



    @ApiOperation(value = "删除账号")
    @PostMapping("/delete/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        umsMemberService.removeById(id);
        return R.ok("删除成功");
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("/get")
    public R<List<UmsMember>> list(@RequestBody UmsMember umsMember) {
        List<UmsMember> umsMemberList = umsMemberService.list(new QueryWrapper<>(umsMember));
        return R.ok(umsMemberList);
    }

    @ApiOperation(value = "列表（分页）")
    @GetMapping("/list")
    public R<IPage<UmsMember>> list(@RequestParam Integer pageNum,@RequestParam Integer limit) {
        IPage<UmsMember> page = umsMemberService.page(pageNum,limit);
        return R.ok(page);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/get/{id}")
    public R<UmsMember> get(@PathVariable("id") String id) {
        UmsMember umsMember = umsMemberService.getById(id);
        return R.ok(umsMember);
    }

    @ApiOperation(value = "修改账号信息")
    @PostMapping("/update")
    public R<String> update(@RequestBody UmsMember umsMember) {
        umsMemberService.updateById(umsMember);
        return R.ok("更新成功");
    }


}
