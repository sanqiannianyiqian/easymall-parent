package cn.tedu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Mangmang
 * @date 2020/2/14 13:08
 */

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.tedu.user.dao")
public class StarterUser {
    public static void main(String[] args) {
        SpringApplication.run(StarterUser.class,args);
    }
}
