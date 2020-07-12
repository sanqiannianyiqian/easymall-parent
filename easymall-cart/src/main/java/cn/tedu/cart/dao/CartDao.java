package cn.tedu.cart.dao;

import com.jt.common.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/20 22:31
 */

public interface CartDao {
    @Select("select * from t_cart where user_id=#{userId}")
    List<Cart> selectCartsByUserId(String userId);//查询

    //新增,新增之前根据用户ID和商品ID判断购物车是否存在该商品
    //不存在就插入,存在就只更新数量
    @Select("select * from t_cart where user_id=#{userId} and product_id=#{productId}")
    Cart selectExistByUIdAndProdId(Cart cart);

    @Update("update t_cart set num=#{num} where user_id=#{userId} and product_id=#{productId}")
    void updateNumByUIdAndProdId(Cart cart);

    void insertCart(Cart cart);

    @Delete(" delete from t_cart where user_id=#{userId} and product_id=#{productId}")
    void deleteCartByUIdAndProdId(Cart cart);
}
