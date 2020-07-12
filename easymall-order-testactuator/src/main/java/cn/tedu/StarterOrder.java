package cn.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@MapperScan("cn.tedu.order.dao")
public class StarterOrder
{
    @Bean
    @LoadBalanced
    //本质是对该对象添加过滤的逻辑，所有这个对象发送
    //的http请求都会被ribbon过滤
    public RestTemplate initTemplate() {
        return new RestTemplate();
    }
    public static void main( String[] args )
    {
        SpringApplication.run(StarterOrder.class, args);
    }
}
