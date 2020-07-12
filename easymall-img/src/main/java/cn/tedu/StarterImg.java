package cn.tedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Mangmang
 * @date 2020/2/11 21:21
 */
@SpringBootApplication
@EnableEurekaClient
public class StarterImg {
    public static void main(String[] args) {
        SpringApplication.run(StarterImg.class, args);
    }
}
