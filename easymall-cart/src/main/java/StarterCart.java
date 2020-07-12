import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mangmang
 * @date 2020/2/18 22:34
 */
@SpringCloudApplication
@EnableEurekaClient
@MapperScan("cn.tedu.cart.dao")
public class StarterCart {
    public static void main(String[] args) {

        SpringApplication.run(StarterCart.class, args);
    }

    //访问商品系统使用的服务调用对象
    @Bean
    @LoadBalanced
    public RestTemplate initRestTemplate() {
        return new RestTemplate();

    }

}
