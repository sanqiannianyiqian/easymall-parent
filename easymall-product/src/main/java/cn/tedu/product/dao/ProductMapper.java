package cn.tedu.product.dao;

import com.jt.common.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/11 13:28
 */
public interface ProductMapper {
    public int selectProductTotal();
    public List<Product> selectProductByPage(@Param("start") Integer start, @Param("rows") Integer rows);
    public Product selectProductById(String productId);
    public void insertProduct(Product product);
    public void updateProductById(Product product);
}
