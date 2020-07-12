package cn.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Mangmang
 * @date 2020/2/26 22:56
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.tedu.search.dao")
public class StarterSearch {
    public static void main(String[] args) {
        SpringApplication.run(StarterSearch.class,args);
    }
}
