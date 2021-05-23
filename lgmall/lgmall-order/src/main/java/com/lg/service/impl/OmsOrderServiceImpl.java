package com.lg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.lg.config.OrderSender;
import com.lg.entity.OmsOrder;
import com.lg.dao.OmsOrderMapper;
import com.lg.entity.OrderProduct;
import com.lg.exception.PriceException;
import com.lg.exception.StockException;
import com.lg.exception.SubmitException;
import com.lg.feign.CartFeign;
import com.lg.feign.ProductFeign;
import com.lg.interceptor.LoginInterceptor;
import com.lg.service.OmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lg.service.OrderProductService;
import com.lg.utils.MemberInfoUtil;
import com.lg.vo.*;
import com.lg.utils.PageUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuguo
 * @since 2021-04-17
 */
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    @Resource
    private ProductFeign productFeign;
    @Resource
    private CartFeign cartFeign;
    @Resource
    private OrderProductService orderProductService;
    @Resource
    private OrderSender orderSender;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 分页列表
     *
     * @param pageNum 当前页
     * @param limit   每页数量
     * @return
     */
    @Override
    public IPage<OmsOrder> page(Integer pageNum, Integer limit) {

        QueryWrapper<OmsOrder> queryWrapper = new QueryWrapper<>();

        return page(PageUtils.getPage(pageNum, limit), queryWrapper);

    }

    /**
     * 详情
     *
     * @param orderSn
     * @return
     */
    @Override
    public OmsOrder info(String orderSn) {
        return this.baseMapper.selectOne(new QueryWrapper<OmsOrder>().eq("order_sn", orderSn));
    }

    /**
     * 新增
     *
     * @param param {table.comment!}对象
     * @return
     */
    @Override
    public void add(OmsOrder param) {

        save(param);
    }

    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public void modify(OmsOrder param) {

        updateById(param);
    }

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */
    @Override
    public void remove(Long id) {
        removeById(id);
    }

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */
    @Override
    public void removes(List<Long> ids) {

        removeByIds(ids);
    }

    /**
     * 生成确认单信息
     *
     * @param list
     * @return
     */
    @Override
    public ConfirmVo confirm(List<cartVo> list) {
        ConfirmVo confirmVo = new ConfirmVo();
        List<Long> list1 = new ArrayList<>(list.size());
        for (cartVo cartVo : list) {
            list1.add(cartVo.getProductId());
        }
        //远程查询仓库
        List<ProductVo> productVos = productFeign.get(list1).getData();
        //检查库存
        for (int i = 0; i < productVos.size(); i++) {
            if (productVos.get(i).getStock() < list.get(i).getCount()) {
                throw new StockException("库存不足");
            }
        }
        confirmVo.setList(productVos);
        //todo 设置当前下单的token，防止重复提交,（或者还有一种是订单号就在这生成，利用数据库的订单id不可重复来实现幂等性）
        String token = MemberInfoUtil.getCurrentMemberId() + System.currentTimeMillis();
        confirmVo.setToken(token);
        //放入redis，下单时在里面拿到和删除
        redisTemplate.opsForValue().set("oms:orderToken", token, 10, TimeUnit.MINUTES);
        return confirmVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submit(SubmitVo submitVo) {
        List<ProductVo> productVoList = submitVo.getProductVoList();
        //todo 提交的幂等性,使用lua脚本原子执行，防止并发问题
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        ArrayList<String> keys = new ArrayList<>(1);
        keys.add(0, "oms:orderToken");
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), keys, submitVo.getToken());
        if (result == null || result == 0) {
            throw new SubmitException("订单提交异常，请重新确认下单");
        }
        //todo 如果远程调用因为各种原因失败了，怎么保证事务的原子性？分布式锁？
        // 比如库存锁定成功了，但是网络异常导致订单服务不知道有没有锁成功，怎么处理？有几种方案的优缺点？
        // 1.使用seata的话,可以保证强一致性，但并发性会大打折扣。
        // 2.通过发送消息给rabbitmq的库存队列，延时个1分钟判断锁定库存对应的订单号是否存在，不存在则解锁库存，实现最终一致性
        //验证价格
        List<Long> productIds = new ArrayList<>(productVoList.size());
        for (ProductVo productVo : productVoList) {
            productIds.add(productVo.getId());
        }
        BigDecimal totalPrice = verifyPrice(productIds, productVoList);
        //锁定库存
        List<cartVo> list1 = new ArrayList<>(productVoList.size());
        for (ProductVo productVo : productVoList) {
            cartVo cartVo = new cartVo();
            cartVo.setProductId(productVo.getId());
            cartVo.setCount(productVo.getStock());
            list1.add(cartVo);
        }
        // todo 用一个分布式id生成器
        String orderSn = Long.toString(System.currentTimeMillis());
        // lockStock里发送锁库存信息给rabbitmq
        LockStockVo lockStockVo = new LockStockVo();
        lockStockVo.setStockItemVoList(list1);
        lockStockVo.setOrderSn(orderSn);
        R response = productFeign.lockStock(lockStockVo);//
        if (response.ok()) {
            //库存锁定成功
            //创建订单，写入数据库
            OmsOrder order = saveOrder(orderSn, totalPrice, productVoList);
            //删除购物车项
            cartFeign.removeItems(productIds);
            //发送到rabbitmq队列，30分钟未支付自动关闭订单，并解锁库存
            orderSender.send(order);
        } else {
            throw new StockException("库存不足");
        }
    }

    private BigDecimal verifyPrice(List<Long> productIds, List<ProductVo> productVoList) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        //远程查询当前最新产品信息
        List<ProductVo> wareList = productFeign.get(productIds).getData();
        for (int i = 0; i < wareList.size(); i++) {
            totalPrice = totalPrice.add(wareList.get(i).getPrice());
            if (!wareList.get(i).getPrice().equals(productVoList.get(i).getPrice())) {
                throw new PriceException("商品价格出现变化，请重新下单");
            }
        }
        return totalPrice;
    }

    private void saveOrderProducts(String orderSn, List<ProductVo> productVoList) {
        List<OrderProduct> orderProducts = new ArrayList<>(productVoList.size());
        for (ProductVo productVo : productVoList) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setCount(productVo.getStock());
            orderProduct.setCreateTime(new Date());
            orderProduct.setCreateTime(new Date());
            orderProduct.setProductId(productVo.getId());
            orderProduct.setOrderSn(orderSn);
            orderProducts.add(orderProduct);
        }
        orderProductService.add(orderProducts);
    }

    private OmsOrder saveOrder(String orderSn, BigDecimal totalPrice, List<ProductVo> productVoList) {
        //获取登录用户id
        Long memberId = Long.parseLong(MemberInfoUtil.getCurrentMemberId());
        OmsOrder order = new OmsOrder();
        order.setMemberId(memberId);
        order.setState(0);
        order.setCreateTime(new Date());
        order.setPayPrice(totalPrice);
        order.setOrderSn(orderSn);
        this.baseMapper.insert(order);
        //将订单商品项存入数据库
        saveOrderProducts(order.getOrderSn(), productVoList);
        return order;
    }


    @Override
    public void closeOrder(OmsOrder order) {
        OmsOrder omsOrder = this.baseMapper.selectOne(new QueryWrapper<OmsOrder>()
                .eq("order_sn", order.getOrderSn()));
        if (omsOrder.getState() == 0) {
            //如果还是未支付状态，那么就关闭订单
            omsOrder.setState(3);
            this.baseMapper.updateById(omsOrder);
            // todo 释放库存

        }
    }

    @Override
    public Boolean existOrder(String orderSn) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<OmsOrder>().eq("order_sn", orderSn));
        return count == 1;
    }
}
