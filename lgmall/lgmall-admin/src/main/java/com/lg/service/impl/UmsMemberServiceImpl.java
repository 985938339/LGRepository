package com.lg.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lg.entity.UmsMember;
import com.lg.dao.UmsMemberMapper;
import com.lg.exception.ApiException;
import com.lg.service.UmsMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.utils.JwtUtils;
import com.lg.utils.PageUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员 服务实现类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-15
 */
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {


    /**
     * 会员分页列表
     *
     * @return
     */
    @Override
    public IPage<UmsMember> page(Integer pageNum, Integer limit) {

        QueryWrapper<UmsMember> queryWrapper = new QueryWrapper<>();

        return page(PageUtils.getPage(pageNum,limit), queryWrapper);

    }

    /**
     * 会员详情
     *
     * @param id
     * @return
     */
    @Override
    public UmsMember info(Long id) {

        return getById(id);
    }

    /**
     * 会员新增
     *
     * @param umsMember {table.comment!}对象
     * @return
     */
    @Override
    public void register(UmsMember umsMember) {
        //密码进行MD5加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bCryptPasswordEncoder.encode(umsMember.getPassword());
        umsMember.setPassword(pwd);
        umsMember.setGender(0);
        umsMember.setCreateTime(new Date());
        save(umsMember);
    }

    @Override
    public String login(UmsMember umsMember)  {
        UmsMember member = baseMapper.selectOne(new QueryWrapper<UmsMember>()
                .eq("username", umsMember.getUsername()));
        if (ObjectUtils.isEmpty(member)) {
            throw new ApiException("账号不存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(umsMember.getPassword(), member.getPassword());
        if (!matches) {
            throw new ApiException("密码错误");
        }
        //使用jwt生成令牌，返回给客户端携带使用
        String token = JwtUtils.GetMemberToken(member.getId().toString(), member.getUsername());
        return token;
    }

    /**
     * 会员修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public void modify(UmsMember param) {

        updateById(param);
    }

    /**
     * 会员删除(单个条目)
     *
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) {
        removeById(id);
    }

    /**
     * 会员删除(多个条目)
     *
     * @param ids
     * @return
     */
    @Override
    public void removes(List<Long> ids) {

        removeByIds(ids);
    }
}
