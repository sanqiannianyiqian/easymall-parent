package cn.tedu.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Mangmang
 * @date 2020/2/28 20:51
 */
public class FanoutMode {
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
    //准备几个静态常亮
    private static final String type="fanout";//交换机类型
    private static final String exName=type+"Ex";//交换机名称
    private static final String q1="queue01"+type;
    private static final String q2="queue02"+type;

    @Test
    public void send() throws IOException {
        String msg = "hello" + type;//消息
        channel.exchangeDeclare(exName, type);//声明交换机
        //声明队列，多个队列，同时绑定一个fanout交换机
        channel.queueDeclare(q1, false, false, false, null);
        channel.queueDeclare(q2, false, false, false, null);
        //绑定
        channel.queueBind(q1, exName, "haha");
        channel.queueBind(q2, exName, "haha");
        //发送消息
        channel.basicPublish(exName, "haha", null, msg.getBytes());

    }
}
