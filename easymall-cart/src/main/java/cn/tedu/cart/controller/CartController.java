package cn.tedu.cart.controller;

import cn.tedu.cart.service.CartService;
import com.jt.common.pojo.Cart;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/20 22:48
 */
@RestController
@RequestMapping("cart/manage/")
public class CartController {
    @Autowired
    CartService cartService;
//查询购物车
    @RequestMapping("query")
    public List<Cart> queryMyCarts(String userId) {
        return cartService.queryMyCarts(userId);
    }

    //新增
    @RequestMapping("save")
    public SysResult addCart(Cart cart) {
        try {
            cartService.addCart(cart);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, "新增购物车失败", null);
        }
    }
    //更新购物车num
    @RequestMapping("update")
    public SysResult updateNum(Cart cart){
        try{
            cartService.updateNum(cart);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201,"更新num失败",null);
        }
    }
    //删除购物车
    @RequestMapping("delete")
    public SysResult deleteCart(Cart cart){
        try{
            cartService.deleteCart(cart);
            return SysResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.build(201,"删除失败",null);
        }
    }
}
