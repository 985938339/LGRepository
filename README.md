# LGRepository

首先要说明的是，这并不是一个完整的项目，其仅仅是作为一个载体，用其中的某些场景来承载自己所用到的某些技术，开发这个项目的目的是用它来展示和记录自己的所用到的一些技术，以及自己对一些技术的理解。

在这个项目中，所涉及到技术栈有：SpringBoot、MybatisPlus、  OpenFeign、Nacos、GateWay、Sentinel、Redis、Mysql、Rabbitmq等。

项目划分为：

1. Common公共模块
2. Member用户成员模块
3. Cart购物车模块
4. Order订单模块
5. Product产品模块
6. GateWay网管模块

各个请求通过nginx代理将其转发到GateWay服务上，再由GateWay服务进行路由转发至各个服务上去。

其中Cart模块中有redisQueue和redisLocker两个包，是自己写的使用队列和使用锁解决缓存数据一致性的两种方案。

Order和Product模块有使用RabbitMq解决分布式事务的最终一致性的实例。

缓存数据不一致发生场景的分析详见：[Redis中的几种更新策略](https://blog.csdn.net/weixin_44228698/article/details/113058160)

redisQueue方案的原理图：

![img](https://img-blog.csdnimg.cn/20210421204851192.PNG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDIyODY5OA==,size_16,color_FFFFFF,t_70#pic_center)

使用多个队列来读写串行化对redis的查询和修改请求。强一致性的保证书库与缓存的一致性。同时具有查询去重的功能防止热点key对数据库造成的缓存击穿。

在这个使用这个方案的过程中也进行了较多的分析，也使用jmeter对其进行了一定的压力测试，详见博客地址：[使用队列实现redis数据一致性的解决方案详解](https://blog.csdn.net/weixin_44228698/article/details/113791793?spm=1001.2014.3001.5502)

redisLocker方案的原理图：

![img](https://img-blog.csdnimg.cn/20210421220005184.PNG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDIyODY5OA==,size_16,color_FFFFFF,t_70#pic_center)

这是对上个方案的一些优化，去掉了底层的线程池，所有的请求都交由自己原来的线程来处理，而不需要另开线程来处理它，最终发现优化完原来采用的就是使用reentrantLock细粒度化锁来实现的。关于从第一种方案到第二种方案的演进，详细的分析过程见博客地址：[对使用队列解决缓存一致性的优化（二）](https://blog.csdn.net/weixin_44228698/article/details/115978358?spm=1001.2014.3001.5501)

当然这两种方案都是实现的强一致性，最终一致性的方案的性能当然会比这要高。