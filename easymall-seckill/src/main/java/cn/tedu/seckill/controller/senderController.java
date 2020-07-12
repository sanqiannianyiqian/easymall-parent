package cn.tedu.seckill.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mangmang
 * @date 2020/2/29 9:55
 */
@RestController
@RequestMapping("")
public class senderController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //接收请求，发送消息
    @RequestMapping("send")
    public String sendMsg(String msg) {
        //直接将消息msg作为消息体的内容，让template发送
        /*
            exchange:交换机名称
            routingKey:路由key
            msg：Object类型msg 自动进行byte转化
         */
//        rabbitTemplate.convertAndSend("directEx","北京",msg);
        //相当于 channel.basicPublish("ex","routing",msg.geBytes)
        //关心发送消息时，消息有一些属性想要携带，使用send方法
        /*MessageProperties properties = new MessageProperties();
        properties.setPriority(100);
        properties.setUserId("110");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.send("directEx1","北京",message);
        return "success";
        上述生产逻辑，是在已存在组件情况下可以实现发送成功
        想要在执行代码的一瞬间，生成需要声明的交换机，队列
        通过配置类，加载到容器内存对象Queue表示队列
        通过配置类，加载到容器内存对象Exchange表示交换机
        在配置类中实现这些声明的组件的对象封装使用@Bean交给容器管理*/

        rabbitTemplate.convertAndSend("seckillD01", "seckill", msg);
        return "success";
    }

}
