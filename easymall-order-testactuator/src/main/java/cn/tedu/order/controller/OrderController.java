package cn.tedu.order.controller;

import cn.tedu.order.service.OrderService;
import com.jt.common.pojo.Order;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/22 12:35
 */
@RestController
@RequestMapping("/order/manage/")
public class OrderController {
@Autowired
    OrderService orderService;
    @RequestMapping("query/{userId}")
    public List<Order> queryOrder(@PathVariable String userId){
        return orderService.selectOrderByUserId(userId);
    }

    @RequestMapping("save")
    public SysResult saveOrder(Order order) {
        try {
            orderService.saveOrder(order);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, "失败了", null);
        }
    }
    @RequestMapping("delete/{orderId}")
    public SysResult deleteOrder(@PathVariable String orderId) {
        try {
            orderService.deleteOrder(orderId);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, "删除失败", null);
        }

    }
}
