package cn.tedu.jedis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.User;

/**
 * @author Mangmang
 * @date 2020/2/14 13:07
 */
public class ObjectMapperTest {
    public static void main(String[] args) {
        //创建一个ObjectMapper
        ObjectMapper om=new ObjectMapper();
        //创建一个用户对象
        User u=new User();
        u.setUserPassword("1111");
        u.setUserName("wang");
        u.setUserId("dskhfaskhf");
        u.setUserEmail("aklsdfhlasd@sadl.com");
        //将user转化成json字符串 {"userId":"","userName":"","userEmail":""}
        try{
            String uJson= om.writeValueAsString(u);
            System.out.println(uJson);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
