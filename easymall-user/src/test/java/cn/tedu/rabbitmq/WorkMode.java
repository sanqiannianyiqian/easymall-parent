package cn.tedu.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Mangmang
 * @date 2020/2/28 20:24
 * /**
 *  资源争抢模式
 *  多个消费端同时监听一个消息队列
 *  */
public class WorkMode {
    private Channel channel;

    @Before
    public void initChannel() throws IOException, TimeoutException {
        //获取连接工厂对象，赋值连接信息 ip port user pw vh
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.42.76.141");
        connectionFactory.setPort(5672);
        //factory.setUsername("guest");
        //factory.getPassword("guest")
       // connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
    }


    @Test
    public void send() throws IOException {
        String msg="小红情书：我就喜欢你每次给我送的大白薯";
        channel.queueDeclare("小红",false,false,false,null);
        for(int i=0;i<100;i++) {
            channel.basicPublish("", "小红", null, msg.getBytes());
        }
    }

    @Test
    public void consumer01() throws IOException, InterruptedException {
        //创建出来消费端对象
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume("小红", true, consumer);
        while (true) {//这个实际中不能这样,要非阻塞异步监听,
            // 不能使用connect对象封装到系统当中需要rabbitmq在springboot已经封装好的自定义配置
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();//从监听的队列中获取一个
//存在的消息
            //delivery接收的除了消息体body以外还有header properties
            System.out.println(("阿明拿到消息："+new String(delivery.getBody())));
        }
    }
    @Test
    public void consumer02() throws IOException, InterruptedException {
        //创建出来消费端对象
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume("小红", true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();//从监听的队列中获取一个
//存在的消息
            //delivery接收的除了消息体body以外还有header properties
            System.out.println(("阿强拿到消息："+new String(delivery.getBody())));
        }
    }
}
