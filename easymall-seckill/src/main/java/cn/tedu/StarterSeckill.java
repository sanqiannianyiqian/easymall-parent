package cn.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("cn.tedu.seckill.dao")
@EnableEurekaClient
public class StarterSeckill {
    public static void main(String[] args) {
        SpringApplication.run(StarterSeckill.class,args);
    }
    @Bean
    public DirectExchange ex(){
        return new DirectExchange("directEx1");
    }
    @Bean
    public Queue queue(){
        return new Queue("queue11");
    }
    @Bean
    public Binding bind(){
        return BindingBuilder.bind(queue()).to(ex()).with("北京");
    }
}
