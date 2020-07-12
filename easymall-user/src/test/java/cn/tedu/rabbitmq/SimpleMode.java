package cn.tedu.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author Mangmang
 * @date 2020/2/28 13:21
 *
 *  * 编写一个方法作为生产端代码
 *  * 编写一个方法作为消费端代码
 *  * 在rabbitmq创建对应的组件 queue
 *  * 实现一发，一接的简单模式
 *
 */
public class SimpleMode {
//实现生产端或者消费端之前，创建连接对象
    private Channel channel;

    //给channel赋值
    @Before
    public void initChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.42.76.141");
        connectionFactory.setPort(5672);
        //factory.setUsername("guest");
        //factory.getPassword("guest")
//        connectionFactory.setVirtualHost("/");//这些有默认设置所以这里不设置
        Connection connection = connectionFactory.newConnection();
         channel = connection.createChannel();
    }
    @Test
    public void send() throws IOException {
        //准备一个即将发送的消息字符串
        String msg = "jjijijijijijijijijilkmoonn";
        //发送之前，声明一个队列
        /*
        queue:String 队列名称
        durable: Boolean 表示队列是否支持持久化
        exclusive: Boolean 表示队列是否专属，专属于当前连接对象Connection
        true 表示操作连接队列只有创建connection才能进行
        autoDelete: Boolean 表示是否自动删除。最后一个channel连接使用完队列
        是否删除 true删除 false不删
        args: Map对象，创建队列的各种属性,例如，消息存活最长时间，队列保存对多消息个数
         */

        //队列和交换机属于vh分配资源，一个队列绑定vh
        //在同一批资源中，队列名称不相同，声明时队列存在，声明无效
        //不存在则创建
        channel.queueDeclare("a", false, false, false, null);
        //channel连接对象的api将消息发送出去
        /*方法参数
        exchange: String 类型代表交换机名称 "" 代表默认AMQP
        routingKey: String 路由key 默认交换机中就是队列名称值
        props: BasicProperties，表示消息的属性
        body: byte[] 消息体的二进制数据
         */
        channel.basicPublish("","a",null,msg.getBytes());
    }

    @Test
    public void consumer() throws InterruptedException, IOException {
        ////创建出来消费端对象
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //consumer绑定channel后就具备了绑定queue的能力
        //绑定消费对象和队列 小红 实现1对1监听
        /*
            autoAck:boolean 自动确认回执
            回执：告诉rabbtimq 是否消费消息成功，返回true
            rabbitmq将会把该消息删除，返回false 该消息继续保存
         */
        channel.basicConsume("a",true,consumer);
        //监听获取消息信息
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();//从监听的队列中获取一个
//存在的消息
        //delivery接收的除了消息体body以外还有header properties
        System.out.println(new String(delivery.getBody()));//body是二进制数组
    }
}
