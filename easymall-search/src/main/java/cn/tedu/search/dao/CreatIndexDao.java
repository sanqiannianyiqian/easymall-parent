package cn.tedu.search.dao;

import com.jt.common.pojo.Product;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/27 16:19
 */
public interface CreatIndexDao {
    List<Product> selectProducts();
}
