package cn.tedu.product.controller;

import cn.tedu.product.service.ProductService;
import com.jt.common.pojo.Product;
import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mangmang
 * @date 2020/2/11 14:28
 */
@RestController
@RequestMapping("product/manage")
public class ProductController {
    @Autowired
    private ProductService productService;
    //商品分页查询
    @RequestMapping("pageManage")
    public EasyUIResult queryProductByPage(Integer page, Integer rows){
        //page是页数，rows条数，
        //封装数据交给业务层
        return productService.queryProductByPage(page,rows);
    }
    //商品单个查询
    @RequestMapping("item/{productId}")
    //url到达springmvc后，都可以使用{}包含一个路径值
    //{值}表示一个变量名称
    public Product queryOneProduct(@PathVariable String productId){
        //利用商品id查询商品对象数据
        return productService.queryOneProduct(productId);
    }
    //商品数据新增
    @RequestMapping("save")
    public SysResult saveProduct(Product product){
        //在业务曾补齐数据新增到数据库
        //SysResult ：
        /*
            status:Integer 表示状态数字，200成功，201失败，202表示其他，203...
            msg:String 表示与前端交互的文字，明确的传递信息。200 msg:太棒了，这次操作
            完成的不错
            data:Object 当ajax请求时查询，可以将查询结果封装到data中，伴随着status，msg将交互
            结构完整的返回
            这个SysResult对象的结构，标准的与ajax对话的结构，基本上任何应用场景，都
            可以使用这个对象。
         */
        try{
            //调用业务层新增商品
            productService.saveProduct(product);
            //返回一个表示操作正常的SysResult status=200
            return SysResult.ok();//status=200 msg=ok data=null;
        }catch(Exception e){
            //进入异常表示新增商品失败
            e.printStackTrace();//打印给自己看
            return SysResult.build(201,"到底怎么回事",null);
        }
    }
    //修改商品
    @RequestMapping("update")
    public SysResult updateProduct(Product product){
        try{
            productService.updateProduct(product);
            return SysResult.ok();
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201,"错了",null);
        }
    }
}
