package ${package.Controller};

import com.lg.constant.Result;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
<#if restControllerStyle>
    import org.springframework.web.bind.annotation.RestController;
<#else>
    import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
    import ${superControllerClassPackage};
</#if>

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/

@Slf4j
@Api(tags = "${table.comment!}")
<#if restControllerStyle>
    @RestController
<#else>
    @Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
        public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
        public class ${table.controllerName} {

        @Resource
        public ${table.serviceName} ${table.entityPath}Service;

        @ApiOperation(value = "新增")
        @PostMapping("/save")
        public R<String> save(@RequestBody ${entity} ${table.entityPath}){
        ${table.entityPath}Service.add(${table.entityPath});
        return R.ok("新增成功");
        }

        @ApiOperation(value = "根据id删除")
        @PostMapping("/delete/{id}")
        public R<String> delete(@PathVariable("id") Long id){
        ${table.entityPath}Service.removeById(id);
        return R.ok("删除成功");
        }

        @ApiOperation(value = "条件查询")
        @PostMapping("/get")
        public R<List<${entity}>> list(@RequestBody ${entity} ${table.entityPath}){
        List<${entity}> ${table.entityPath}List = ${table.entityPath}Service.list(new QueryWrapper<>(${table.entityPath}));
        return R.ok(${table.entityPath}List);
        }

        @ApiOperation(value = "列表（分页）")
        @GetMapping("/list")
        public R<IPage<${entity}>> list(@RequestParam Integer pageNum,@RequestParam Integer limit){
        IPage<${entity}> page = ${table.entityPath}Service.page(pageNum,limit);
        return R.ok(page);
        }

        @ApiOperation(value = "详情")
        @GetMapping("/get/{id}")
        public R<${entity}> get(@PathVariable("id") Long id){
        ${entity} ${table.entityPath} = ${table.entityPath}Service.getById(id);
        return R.ok(${table.entityPath});
        }

        @ApiOperation(value = "根据id修改")
        @PostMapping("/update")
        public R<String> update( @RequestBody ${entity} ${table.entityPath}){
        ${table.entityPath}Service.updateById(${table.entityPath});
        return R.ok("更新成功");
        }

    </#if>

    }
</#if>