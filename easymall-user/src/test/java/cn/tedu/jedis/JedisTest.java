package cn.tedu.jedis;

import com.jt.common.utils.UUIDUtil;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/14 12:51
 */
public class JedisTest {
    /*
          如何使用jedis链接到对应的redis服务端
       */
    @Test
    public void test01() {
        //相当于从java代码执行 redis-cli -h 10.9.104.184 -p 6379
        Jedis jedis = new Jedis("10.42.76.141", 6379);
        //可以利用jedis对象调用api方法操作redis
        jedis.set("name", "王老师");
        System.out.println(jedis.get("name"));
        jedis.exists("name");
        jedis.lpush("list01", "1");
        jedis.rpop("list01");
        jedis.hset("user", "age", "18");
    }

    //分片计算逻辑测试
    @Test
    public void test() {
        //创建一个分片对象，提供3个节点的信息
        List<Jedis> nodes = new ArrayList<Jedis>();
        nodes.add(new Jedis("10.42.76.141", 6379));
        nodes.add(new Jedis("10.42.76.141", 6380));
        nodes.add(new Jedis("10.42.76.141", 6381));
        MyShardedJedis msj = new MyShardedJedis(nodes);
        //使用for循环模拟大量不同key-value数据的生成
        for (int i = 0; i < 100; i++) {
            //每循环一次，模拟系统需要操作redis分布式集群处理的数据一次
            String key = UUIDUtil.getUUID() + i;
            String value = "name" + i;
            msj.set(key, value);
            System.out.println(msj.get(key));
        }
    }

    @Test
    public void test03() {
        //创建一个可以实现分片计算的对象ShardedJedis
        //收集节点信息，交给分片对象
        List<JedisShardInfo> nodes = new ArrayList<>();
        //6379,6380 6381节点信息存到nodes中
        nodes.add(new JedisShardInfo("10.42.76.141", 6379, 500, 500, 5));
        nodes.add(new JedisShardInfo("10.42.76.141", 6380));//weigth=1
        nodes.add(new JedisShardInfo("10.42.76.141", 6381));//wegith=1
        //构造一个分片对象ShardedJedis
        ShardedJedis sj = new ShardedJedis(nodes);
        for (int i = 0; i < 1000; i++) {
            String key = UUIDUtil.getUUID();
            String value = "" + i;
            sj.set(key, value);
            System.out.println(sj.get(key));
        }
    }

    @Test
    public void test04() {
        //创建连接池对象，先准备一个连接池属性配置对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //定义最大连接数 最小空闲，最大空闲，初始化等等
        jedisPoolConfig.setMaxTotal(200);
        //最小空闲数,不满足最小空闲说明忙，按照最大空闲的上限，补充jedis
        jedisPoolConfig.setMaxIdle(8);
        //最小空闲数,不满足最小空闲说明忙，按照最大空闲的上限，补充jedis
        jedisPoolConfig.setMinIdle(3);
        //使用config对象创建jedisPool
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "10.42.76.141", 6380);
        //连接池底层就是按照给定属性，创建一批连接6380的jedis
        Jedis resource = jedisPool.getResource();//相当于从连接池拿到一个空闲的jedis对象
        resource.set("name", "王老师");
        jedisPool.returnResource(resource);

    }
}
