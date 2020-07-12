package cn.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Mangmang
 * @date 2020/2/11 13:22
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.tedu.product.dao")
public class StarterProduct {
    public static void main(String[] args) {
        SpringApplication.run(StarterProduct.class, args);
    }
}
