package cn.tedu.seckill.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Mangmang
 * @date 2020/2/29 11:01
 * 可以实现争抢
 */
@Component
public class SeckillConsumer2 {//可以实现争抢
    //任意编辑一个方法，实现消费逻辑
    //方法的参数就是发送到rabbitmq中的对象
    //可以String 接收body 也可以是Message接收
    //包含消息属性
    /*@RabbitLisener 监听注解
在方法上，添加这个注解，启动容器时能够扫描到Component，也扫描到了RabbitLisener
可以通过属性指定该方法对应消费者监听的队列命令。
*/
    @RabbitListener(queues = "seckill01")
    public void consum(String msg) {
        //执行消费逻辑
        System.out.println("消费者2接收到的消息:"+msg);

    }
}
