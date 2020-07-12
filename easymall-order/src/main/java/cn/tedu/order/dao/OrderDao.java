package cn.tedu.order.dao;

import com.jt.common.pojo.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/22 12:36
 */
public interface OrderDao {
    List<Order> selectOrderByUserId(String orderId);

    void saveOrder(Order order);

    void deleteOrder(String orderId);
 }
