package cn.tedu.seckill.controller;

import cn.tedu.seckill.dao.SeckillDao;
import com.jt.common.pojo.Seckill;
import com.jt.common.pojo.Success;
import com.jt.common.vo.SysResult;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/29 9:16
 */
@RestController
@RequestMapping("/seckill/manage")
public class SeckillController {
    //减少代码编写的业务层
    @Autowired
    private SeckillDao seckillDao=null;

    //查询所有的秒杀商品数据
    @RequestMapping("list")
    public List<Seckill> queryAll() {
       return seckillDao.selectAll();
    }
    //根据id查询秒杀商品数据
    @RequestMapping("detail")
    public Seckill queryOne(String seckillId) {
        return seckillDao.selectOne(seckillId);
    }
    //作为前端系统接收秒杀请求，第一时间
    //封装好参数，作为消息发送给交换机

    @Autowired
    private RabbitTemplate template;
    @RequestMapping("{seckillId}")
    public SysResult seckill(@PathVariable Long seckillId) {
        //封装消息，表示谁秒杀了什么
        //如果是从登陆状态获取用户信息，cookie获取，连接redis
        //拿到userJson 解析数据userId等 模拟一个电话号
        String userPhone="1330011"+ RandomUtils.nextInt(9999);
        //判断当前用户userPhone是否在redis有数据，有则秒杀过
        //拒绝发送消息，秒杀过了，没有就新用户
        //将表示用户身份的电话拼接seckillId后
        String msg=userPhone+"/"+seckillId;
        template.convertAndSend("seckillD01","seckill",msg);
        //jedis.set("userPhone","1")
        return SysResult.ok();

//老师讲的判断redis中是否队列不为nil是在消费端,其实应该在生产端就先判断,如果生产端判断库存预减一已经小于零了就不用发送消息了,
// 如果大于零就发送消息,这样消费端就会一直异步消费消息执行真实减库存
    }
    //展示成功者信息
    @RequestMapping("{seckillId}/userPhone")
    public List<Success> querySuccess(@PathVariable
                                              Long seckillId){
        return seckillDao.selectSuccess(seckillId);
    }



}
