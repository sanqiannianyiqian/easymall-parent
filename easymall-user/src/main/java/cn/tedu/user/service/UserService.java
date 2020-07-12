package cn.tedu.user.service;

import cn.tedu.user.dao.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.User;
import com.jt.common.utils.MD5Util;
import com.jt.common.utils.MapperUtil;
import com.jt.common.utils.UUIDUtil;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper = null;
    //    @Autowired
//    private ShardedJedisPool pool;
    @Autowired
    private JedisCluster jedis;


    public SysResult checkUserName(String userName) {
        //从数据库读取数据判断是否可用
        int exist = userMapper.selectCountByUserName(userName);
        //根据返回结果返回SysResult的数据
        if (exist == 1) {//已经存在 不可用 status=201
            return SysResult.build(201, "", null);
        } else {
            //表示等于0 可用 status=200
            return SysResult.ok();
        }
    }

    //注册用户
    public void saveUser(User user) {
        //user对象作为表单数据的接收,缺少userId
        String userId = UUIDUtil.getUUID();
        user.setUserId(userId);
        String pasMd5 = MD5Util.md5(user.getUserPassword());
        user.setUserPassword(pasMd5);
        userMapper.insertUser(user);
    }

    public String doLogin(User user) {
        // ShardedJedis jedis = pool.getResource();
           /*
        1 判断合法
        2 存储redis
         */
        // 准备一个返回的字符串，默认值""
        String ticket = "";
        //使用user参数 查询数据库，判断是否存在user行数据 验证合法
        //user里明文password加密
        user.setUserPassword(MD5Util.md5(user.getUserPassword()));
        User userExist = userMapper.selectUserByNameAndPw(user);////select * from t_user where name= and pw=
        if (userExist == null) {
            return ticket;  //说明没有查询到user对象登陆时不合法的
        } else {
            //说明合法数据
            //创建key-value的数据将用户信息存储在redis,供客户端后续访问使用
            //准备一个数据 userJson字符串 可以将密码做空 redis的value
            //调用一个对象的api方法 ObjectMapper 可以将json和对象进行相互转化 writeValueAsString
            //readValue
            // 给ticket赋值，ticket体现特点：用户不同时，ticket要不同，同一个用户，不同时间登陆，生成的ticket也不同
            ticket = "EM_TICKET" + System.currentTimeMillis() + user.getUserName();
            //生成顶替逻辑中的userId的唯一值
            String loginKey = "login_" + userExist.getUserId();
            //判断是否需要顶替
            if (jedis.exists(loginKey)) {
                //进入到if说明有人已经登录，要将它登录时使用的ticket删掉
                String lastTicket = jedis.get(loginKey);
                jedis.del(lastTicket);
                //在这个loginKey覆盖上次登录有效ticket
                jedis.set(loginKey, ticket);
            }//如果不存在，就把自己登录时用2个key-value设置在redis
            jedis.set(loginKey, ticket);

            userExist.setUserPassword(null);//cgb1908讲的要对敏感信息进行脱敏,比如密码可能就是这里吧,那边是把密码设置为123456
            ObjectMapper objectMapper = MapperUtil.MP; //将exist 转化成字符串
            //准备好链接redis的对象jedis
            //Jedis jedis = new Jedis("10.42.76.141", 6380);
            try {
                String uJson = objectMapper.writeValueAsString(userExist);
                //需要存储在redis的key值和value都准备号了，下面可以set方法调用6380
                jedis.setex(ticket, 60 * 60 * 2, uJson);//不能set永久数据，假设超时时间2小时
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "";
            }/*finally {
                if (jedis!=null) {
                    //jedis.close();
                    pool.returnResource(jedis);
                }
            }*/
            return ticket;
        }
    }

    public String queryUserData(String ticket) {//进入某些页面会调用这个方法,通过ajax
        //创建jedis对象
        //      ShardedJedis jedis = pool.getResource();
        // Jedis jedis = new Jedis("10.42.76.141", 6380);
        //保证使用完毕，关闭对象
        try {
            Long leftTime = jedis.pttl(ticket);//以毫秒为单位返回 key 的剩余过期时间。
            if (leftTime < 1000 * 60 * 30) {
                //剩余时间不足30分钟，重新刷一次超时新的2小时
                //延长 leftTime+想延长时间
                jedis.pexpire(ticket, 1000 * 60 * 60 * 2);
            }
            //get方法返回get数据
            return jedis.get(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } /*finally {
            if (jedis!=null) {
              //  jedis.close();
                pool.returnResource(jedis);
            }*/
    }
}

