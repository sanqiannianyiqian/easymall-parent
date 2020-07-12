package cn.tedu.order.service;

import cn.tedu.order.dao.OrderDao;
import com.jt.common.pojo.Order;
import com.jt.common.utils.UUIDUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/22 12:36
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @HystrixCommand//断路或者故障后
    public List<Order> selectOrderByUserId(String userId) {
        //查询
        //todo 未找到结果?
        List<Order> orders = orderDao.selectOrderByUserId(userId);//那一次连接换一次连接
         /*//挨个封装
         优点：如果2个相关数据的表格没有按照ER分片表，或者使用mycat无法管理2个相关表格的相关数据，可以在业务层使用业务逻辑代码实现访问数据库数据
        缺点：影响这个方法执行的性能。
        for(Order order:oList){
            //每循环一个 对应使用orderId查询子表
            //select * from t_order_item where order_id=
            List<OrderItem> oiList=om.selectOrderItem(order.getOrderId());//拿一次连接，还一次连接,所以效率低
            order.setOrderItems(oiList);
        }*/
        return orders;
    }

    public void saveOrder(Order order) {
        //缺少orderId
        order.setOrderId(UUIDUtil.getUUID());
        order.setOrderPaystate(0);
        order.setOrderTime(new Date());
        orderDao.saveOrder(order);
          /*//order对象中的orderItems 元素对象orderItem也需要
        //orderId属性值。
        om.insertOrder(order);//写主表
        for(OrderItem oi:order.getOrderItems()){
            //oi对应t_order_item中的一个行数据
            //每个oi对象都缺少orderId
            oi.setOrderId(order.getOrderId());
            //新增oi到t_order_item
            om.insertOrderItem(oi);
            通过SQL级联实现
        }*/
    }

    @Autowired
    RestTemplate restTemplate;
    public void deleteOrder(String orderId) {
        orderDao.deleteOrder(orderId);
    }
    @HystrixCommand(fallbackMethod = "error")//断路或者故障后
    //fallback回调方法 方法名称error，需要根据配置名称
    //在本类中实现一个和调用方法结构完全一致的方法
    public String sayHello(String name){
        //需要在业务层想办法调用service-hi的功能
        //通过治理组件注册发现，可以直接调用service-hi
        String result= restTemplate.getForObject
                ("http://service-hi/client/hello?name="+name,
                        String.class);
        //result hello i am from 9001/9002
        return result;
    }
}
