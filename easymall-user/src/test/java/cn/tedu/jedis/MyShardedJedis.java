package cn.tedu.jedis;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/14 20:57
 */
public class MyShardedJedis {
    //属性，list节点信息，保管了所有的集群节点内容
    //有三个节点 list元素3个，5个节点 list元素5个
    List<Jedis> nodes=new ArrayList<>();
    //构造方法，每次构造使用这个对象，必须传递所有节点的信息。
    public MyShardedJedis(List<Jedis> nodes){
        this.nodes=nodes;
        //nodes={new Jedis(6379),new Jedis(6380),new Jedis(6381)};
        //下标 取余结果0 1 2
    }
    //封装一个hash取余的算法，对任意key值，找到对应节点
    public Jedis keyAndNode(String key){
        //hash取余公式 name 取余 0
        int result=(key.hashCode()&Integer.MAX_VALUE)%nodes.size();
        return nodes.get(result);
    }
    //只要对jedis的所有操作key值的方法重新封装一遍，当前分片对象可以使用了
    //get set
    public String get(String key){
        //传进来的key对应了哪个节点
        Jedis jedis = keyAndNode(key);
        return jedis.get(key);
    }
    public void set(String key,String value){
        //传进来的key对应了哪个节点
        Jedis jedis = keyAndNode(key);
        jedis.set(key,value);
    }
    public boolean exists(String key){
        //传进来的key对应了哪个节点
        Jedis jedis = keyAndNode(key);
        return jedis.exists(key);
    }
    //....
}
