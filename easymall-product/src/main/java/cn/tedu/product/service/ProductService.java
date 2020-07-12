package cn.tedu.product.service;

import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;

/**
 * @author Mangmang
 * @date 2020/2/11 14:12
 */
public interface ProductService {
    public Product queryOneProduct(String productId);
    public EasyUIResult queryProductByPage(Integer page, Integer rows);


    public void saveProduct(Product product);
    public void updateProduct(Product product);
}