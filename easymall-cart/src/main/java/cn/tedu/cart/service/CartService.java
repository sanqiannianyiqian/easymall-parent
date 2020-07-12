package cn.tedu.cart.service;

import cn.tedu.cart.dao.CartDao;
import com.jt.common.pojo.Cart;
import com.jt.common.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/20 22:45
 */
@Service
public class CartService {
    @Autowired
    private CartDao cartDao;
//查询购物车
    public List<Cart> queryMyCarts(String userId) {
        return cartDao.selectCartsByUserId(userId);
    }

    //新增
    @Autowired
    RestTemplate restTemplate;

    public void addCart(Cart cart) {
        //看当前add是insert还是updatenum
        //利用传递的参数 userId productId num select exist from t_cart
        Cart existCart = cartDao.selectExistByUIdAndProdId(cart);
        //如果不存在
        if (existCart == null) {
            //从商品微服务调用服务获取商品信息
            String url="http://productservice/product/manage/item/"+cart.getProductId();
            Product product = restTemplate.getForObject(url, Product.class);
            cart.setProductName(product.getProductName());
            cart.setProductImage(product.getProductImgurl());
            cart.setProductPrice(product.getProductPrice());
            cartDao.insertCart(cart);
        } else {//如果存在
            //传递的购物车数据可以在数据库查到，将旧num和新num整合更新数据
            int newNum = existCart.getNum() + cart.getNum();
            cart.setNum(newNum);//cart中保存了合并的num值
            cartDao.updateNumByUIdAndProdId(cart);
        }
    }
    //更新
    public void updateNum(Cart cart){
        cartDao.updateNumByUIdAndProdId(cart);
    }

    //删除
    public void deleteCart(Cart cart){
        cartDao.deleteCartByUIdAndProdId(cart);
    }

}
