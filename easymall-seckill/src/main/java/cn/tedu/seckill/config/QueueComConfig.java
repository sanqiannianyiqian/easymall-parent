package cn.tedu.seckill.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mangmang
 * @date 2020/2/29 10:36
/*
实现一个配置类编写
通过声明大量的对象，使得连接rabbitmq的底层
conncetion可以创建很多组件内容
和调用底层代码queueDeclear exchangeDeclear效果一样
 */
@Configuration
public class QueueComConfig {
    //声明队列对象
    @Bean
    public Queue queue01() {
        //springboot在底层通过连接
        //调用queueDeclear name false false false null
        return new Queue("seckill01", false, false, false, null);
    }
    @Bean
    public Queue queue02(){
        //springboot在底层通过连接
        //调用queueDeclear name false false false null
        return new Queue("seckill02",false,false,false,null);
    }
    //配置声明交换机对象
    @Bean
    public DirectExchange exD1(){
        return new DirectExchange("seckillD01");//默认不自动删除，默认持久化
    }
    //配置声明交换机对象
    @Bean
    public DirectExchange exD2(){
        return new DirectExchange("seckillD02");//默认不自动删除，默认持久化
    }
    @Bean
    public Binding bind01(){
        return BindingBuilder.bind(queue01()).to(exD1()).with("seckill");
        //seckill01使用seckill的路由绑定到了seckillD01
    }
    @Bean
    public Binding bind02(){
        return BindingBuilder.bind(queue02()).
                to(exD2()).with("haha");
        //seckill02使用haha的路由绑定到了seckillD02
    }


}
