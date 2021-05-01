package com.lg.service;

import com.lg.entity.UmsMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员 服务类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-15
 */
public interface UmsMemberService extends IService<UmsMember> {

    /**
     * 会员分页列表

     */
    IPage<UmsMember> page(Integer pageNum, Integer limit);

    /**
     * 会员详情
     *
     * @param id
     * @return
     */
    UmsMember info(Long id);

    /**
     * 会员新增
     *
     * @param param 根据需要进行传值
     * @return
     */
    void register(UmsMember param);

    /**
     * 会员修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    void modify(UmsMember param);

    /**
     * 会员删除(单个条目)
     *
     * @param id
     * @return
     */
    void remove(Long id);

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    void removes(List<Long> ids);

    String login(UmsMember umsMember) throws Exception;
}
