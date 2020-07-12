package cn.tedu.product.service;

import cn.tedu.product.dao.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import com.jt.common.utils.UUIDUtil;
import com.jt.common.vo.EasyUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/11 14:18
 */
@Service
public class ProductServiceImpl implements ProductService {
@Autowired
ProductMapper productMapper=null;
    @Autowired
    private JedisCluster cluster;

    @Override
    public Product queryOneProduct(String productId) {
        //调用持久层mapper 发送sql语句封装product对象
        //TODO 商品单个查询缓存逻辑，提升查询效率
        //product数据在redis存储结构 string类型 json key值保证商品数据唯一性 productId
        String productKey="product_"+productId;
        //使用集群判断商品数据是否存在
        try {
            ObjectMapper mp= MapperUtil.MP;
            if (cluster.exists(productKey)) {
                //if如果进入，说明能够从redis集群获取商品数据
                //可以通过key值返回json字符串
                String productJson = cluster.get(productKey);
                //将json转化为对象
                Product product = mp.readValue(productJson, Product.class);;//pJson源数据，Product.class
                //把json转化回来的类型反射对象
                return product;
            }else {
                //说明缓存未命中，到数据库查询数据
                Product product = productMapper.selectProductById(productId);
                //不着急返回数据，将product对象转化为json放到redis中
                String productJson = mp.writeValueAsString(product);
                cluster.setex(productKey,60*60*24,productJson);
                //查询结果返回给前端
                return product;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EasyUIResult queryProductByPage(Integer page, Integer rows) {
        //准备一个空对象
        EasyUIResult result=new EasyUIResult();
        /*
        int total:封装分页查询时对应表格数据的全部商品个数
        List rows:利用持久层从数据库查询分页数据的包装对象
         */
        //total 总数 select count(*) from t_product
        int total=productMapper.selectProductTotal();//65
        result.setTotal(total);
        //利用page rows做分页查询语句，查询List<Product>
        //计算起始位置
        int start=(page-1)*rows;
        List<Product> pList=productMapper.selectProductByPage(start,rows);
        result.setRows(pList);
        return result;
    }

    @Override
    public void saveProduct(Product product) {
        //补齐id值
        String productId= UUIDUtil.getUUID();
        //一台服务器生成的uuid每次一定不一样的
        //服务器集群生成uuid有可能一样（几率极低）
        product.setProductId(productId);
        productMapper.insertProduct(product);
        //新增数据可以看成绝大多数都是热点数据。可以在新增时直接添加缓存逻辑
        // redis缓存
        try {
            ObjectMapper mp= MapperUtil.MP;
            String productKey = "product_" + productId;
            String productJson = mp.writeValueAsString(product);//转化json
            cluster.setex(productKey, 60 * 60 * 24, productJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        //TODO redis缓存，高并发下保证缓存与数据库数据一致
        //保证更新商品与缓存数据一致
        //更新之前将商品缓存数据删除
        String productKey="product_"+product.getProductId();
        cluster.del(productKey);
        productMapper.updateProductById(product);
    }
}
