package com.lg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lg.entity.CmsCartItem;
import com.lg.dao.CmsCartItemMapper;
import com.lg.feign.ProductFeign;
import com.lg.redisQueue.RedisQueueService;
import com.lg.interceptor.LoginInterceptor;
import com.lg.service.CmsCartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.utils.MemberInfoUtil;
import com.lg.utils.PageUtils;
import com.lg.vo.ProductVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Service
public class CmsCartItemServiceImpl extends ServiceImpl<CmsCartItemMapper, CmsCartItem> implements CmsCartItemService {
    @Resource
    private RedisQueueService redisQueueService;
    @Resource
    private RedisTemplate<String, Map<String, CmsCartItem>> redisTemplate;
    @Resource
    private ProductFeign productFeign;

    private String getKey() {
        return "cms:cart:" + MemberInfoUtil.getCurrentMemberId();
    }

    /**
     * 分页列表
     *
     * @param pageNum 查询当前页数
     * @param limit   每页显示数量
     * @return
     */
    @Override
    public IPage<CmsCartItem> page(Integer pageNum, Integer limit) throws Throwable {
        IPage<CmsCartItem> result = new Page<>();
        //查询缓存为空
        String key = getKey();
        //在队列里面查询
        result = redisQueueService.query(key, 2000, key1 -> {
            IPage<CmsCartItem> page1 = new Page<>();
            Map map = redisTemplate.opsForHash().entries(getKey());
            List<CmsCartItem> list = new ArrayList<>(map.values()).subList(pageNum * limit, limit > map.size() ? map.size() : (pageNum + 1) * limit);
            page1.setRecords(list);
            return page1;
        }, (key2) -> {
            Page<CmsCartItem> page = new Page<>();
            //二次检查redis是否有值，查询到就直接返回结果
            Map<String, CmsCartItem> map2 = redisTemplate.opsForValue().get(getKey());
            if (map2 != null) {
                page.setRecords(new ArrayList<>(map2.values()));
                return page;
            }
            //查询数据库中的购物车信息
            QueryWrapper<CmsCartItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("member_id", MemberInfoUtil.getCurrentMemberId());
            page = page(PageUtils.getPage(pageNum, limit), queryWrapper);
            //把购物车信息存入redis中
            map2 = new HashMap<>();
            for (CmsCartItem cartItem : page.getRecords()) {
                map2.put(cartItem.getId().toString(), cartItem);
            }
            redisTemplate.opsForHash().putAll(key2, map2);
            return page;
        });
        return result;
    }

    @Override
    public List<CmsCartItem> itemList() throws Throwable {
        List<CmsCartItem> result = null;
        String key = getKey();
        result = redisQueueService.query(key, 2000, key1 -> {
            Map<String, CmsCartItem> map = redisTemplate.opsForValue().get(getKey());
            return new ArrayList<>(new LinkedList<>(map.values()));
        }, (key2) -> {
            //二次检查redis是否有值，查询到就直接返回结果
            Map<String, CmsCartItem> map2 = redisTemplate.opsForValue().get(getKey());
            if (map2 != null) {
                return new ArrayList<>(map2.values());
            }
            //查询数据库中的购物车信息
            QueryWrapper<CmsCartItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("member_id", MemberInfoUtil.getCurrentMemberId());
            List<CmsCartItem> list = this.baseMapper.selectList(queryWrapper);
            //把查到的购物车信息存入redis中
            map2 = new HashMap<>();
            for (CmsCartItem cartItem : list) {
                map2.put(cartItem.getId().toString(), cartItem);
            }
            redisTemplate.opsForHash().putAll(key2, map2);
            //返回结果
            return list;
        });
        return result;

    }

    /**
     * 查询单个购物项详情
     *
     * @param id
     * @return
     */
    @Override
    public CmsCartItem info(Long id) throws Throwable {
        CmsCartItem result = null;
        result = redisQueueService.query(getKey(), 2000, key1 -> {
            return (CmsCartItem) redisTemplate.opsForHash().get(getKey(), id);
        }, (key2) -> {
            //二次检查redis是否有值，查询到就直接返回结果
            CmsCartItem cartItem1 = (CmsCartItem) redisTemplate.opsForHash().get(key2, id);
            if (cartItem1 != null) {
                return cartItem1;
            }
            //查询数据库中的购物车信息
            QueryWrapper<CmsCartItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id).eq("member_id", MemberInfoUtil.getCurrentMemberId());
            cartItem1 = this.baseMapper.selectOne(queryWrapper);
            //把查到的购物车信息存入redis中
            redisTemplate.opsForHash().put(key2, id, cartItem1);
            //返回结果
            return cartItem1;
        });
        return result;
    }

    /**
     * 新增
     *
     * @param ids 商品id列表
     * @return
     */
    @Override
    public void add(List<Long> ids) throws Throwable {
        List<ProductVo> list = productFeign.get(ids).getData();
        if (ObjectUtils.isEmpty(list)) {
            return;
        }
        List<CmsCartItem> itemList = new ArrayList<>(list.size());
        for (ProductVo p : list) {
            CmsCartItem cartItem = new CmsCartItem();
            cartItem.setMemberId(Long.parseLong(MemberInfoUtil.getCurrentMemberId()));
            cartItem.setCreateTime(new Date());
            cartItem.setUpdateTime(new Date());
            cartItem.setProductId(p.getId());
            cartItem.setProductName(p.getName());
            cartItem.setPrice(p.getPrice());
            cartItem.setCount(1);
            itemList.add(cartItem);
        }
        redisQueueService.update(getKey(), itemList, 1000, (key, value) -> {
            //更新数据库
            saveBatch(value);
            //更新redis
            redisTemplate.opsForHash().delete(key);
        });
    }

    /**
     * 修改
     *
     * @param cartItem 根据需要进行传值
     * @return
     */
    @Override
    public void modify(CmsCartItem cartItem) throws Throwable {
        cartItem.setUpdateTime(new Date());
        redisQueueService.update(getKey(), cartItem, 1000, (key, value) -> {
            //更新数据库
            updateById(cartItem);
            //更新redis
            redisTemplate.opsForHash().delete(key);
        });
    }

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) throws Throwable {
        redisQueueService.update(getKey(), id, 1000, (key, value) -> {
            //更新数据库
            removeById(id);
            //更新redis
            redisTemplate.opsForHash().delete(key);
        });
    }

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    @Override
    public void removes(List<Long> ids) throws Throwable {
        redisQueueService.update(getKey(), ids, 1000, (key, value) -> {
            //更新数据库
            removeByIds(ids);
            //更新redis
            redisTemplate.opsForHash().delete(key);
        });
    }

    @Override
    public void removeItems(List<Long> productIds) throws Throwable {
        redisQueueService.update(getKey(), productIds, 1000, (key, value) -> {
            //更新数据库
            QueryWrapper<CmsCartItem> wrapper = new QueryWrapper();
            wrapper.in("product_id", value);
            this.baseMapper.delete(wrapper);
            //更新redis
            redisTemplate.opsForHash().delete(key);
        });
    }
}
