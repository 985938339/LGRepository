package com.lg;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;

public class MybatisGenerite {
    public static void main(String[] args) {

        AutoGenerator mpg = new AutoGenerator();
        // 配置策略
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");// 当前项目的路径
        gc.setOutputDir(projectPath + "/lgmall-order/src/main/java");// 生成文件输出根目录
        gc.setAuthor("liuguo");// 作者
        gc.setOpen(false); // 生成完成后不弹出文件框
        gc.setFileOverride(true); // 文件是否覆盖
        gc.setIdType(IdType.ASSIGN_UUID); //主键策略 实体类主键ID类型
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); // 是否开启swagger
        gc.setActiveRecord(true); //【不懂】 活动记录 不需要ActiveRecord特性的请改为false 是否支持AR模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);//【不懂】 XML ResultMap xml映射文件的配置
        gc.setBaseColumnList(false);//【不懂】 XML columList xml映射文件的配置

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        mpg.setGlobalConfig(gc);

        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://192.168.56.10:3306/lgmall_oms?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        //3、包的配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com");
        pc.setController("controller"); // 可以不用设置，默认是这个
        pc.setService("service"); // 同上
        pc.setServiceImpl("service.impl"); // 同上
        pc.setMapper("dao"); // 默认是mapper
        pc.setEntity("entity"); // 默认是entity
        pc.setXml("mapper.xml"); // 默认是默认是mapper.xml
        pc.setModuleName("lg"); // 控制层请求地址的包名显示
        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("mq_message"); // 逆向工程使用的表   如果要生成多个,这里可以传入String[]
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); // 自动lombok；
        strategy.setCapitalMode(false); // 开启全局大写命名
        strategy.setSuperMapperClass(null); //

        strategy.setRestControllerStyle(false); // 控制：true——生成@RsetController false——生成@Controller
        strategy.setControllerMappingHyphenStyle(true); // 【不知道是啥】
        strategy.setEntityTableFieldAnnotationEnable(true); // 表字段注释启动 启动模板中的这个 <#if table.convert>
        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // 是否删除实体类字段的前缀
        strategy.setControllerMappingHyphenStyle(false); // 控制层mapping的映射地址 false：infRecData true：inf_rec_data
        mpg.setStrategy(strategy);

        //模板生成器
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/freemaker/controller.java");
        tc.setService("/templates/freemaker/service.java");
        tc.setServiceImpl("/templates/freemaker/serviceImpl.java");
        tc.setEntity("/templates/freemaker/entity.java");
//        tc.setMapper("/templates/freemaker/mapper.java");
//        tc.setXml("/templates/freemaker/mapper.xml");
        mpg.setTemplate(tc);

        mpg.execute(); //执行
    }
}
