package cn.tedu.lucene;

import com.jt.common.pojo.Product;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 模拟lucene创建索引，封装数据源过程
 */
public class CreateIndex {
    @Test
    public void createIndex()throws Exception{
        //读取数据源。String模拟
        //商品数据
        Product p1=new Product();
        p1.setProductId("1");
        p1.setProductCategory("手机");
        p1.setProductDescription("3亿像素，高清摄影，照亮景色的美");
        p1.setProductName("达内PN3 pro手机");
        p1.setProductNum(500);
        Product p2=new Product();
        p2.setProductId("2");
        p2.setProductCategory("电脑");
        p2.setProductDescription("游戏发烧的选择，泰坦2080TI");
        p2.setProductName("达内杀手3000");
        p2.setProductNum(200)

        ;
        //准备索引文件的输出目录
        Path path = Paths.get("D:/index01");//索引文件目录
        FSDirectory dir=FSDirectory.open(path);
        //给writer输出流配置一个配置对象
        IndexWriterConfig config=new
                IndexWriterConfig(new IKAnalyzer6x());
        //定义创建索引的模式追加-覆盖
        //create 覆盖，每次执行都会对已有的同名目录做覆盖
        //append追加，同名文件夹存在，会对数据追加
        //create_or_append 有则追加，无则创建
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建输出流对象
        IndexWriter writer=new IndexWriter(dir,config);
        //封装document 从2个原数据p1 p2封装2个索引需要的doc数据
        //doc的数据结构需要和原数据一致，自定义doc中属性
        //属性分为2大类，一类字符串类，一类数字类
        Document doc1=new Document();
        Document doc2=new Document();
        //TextField StringField存储字符串数据
        //name自定义属性名称
        //value：属性值
        //store: 表示是否存储在索引中
        doc1.add(new TextField("productId",p1.getProductId(),
                Field.Store.YES));
        doc1.add(new TextField("productCat",p1.getProductCategory(),
                Field.Store.YES));
        doc1.add(new TextField("productDesc",p1.getProductDescription(),
                Field.Store.YES));
        doc1.add(new TextField("productName",p1.getProductName(),
                Field.Store.YES));
        //为了搜索时能够使用num 同名field 字符串类型数据
        doc1.add(new StringField("num","500个",Field.Store.YES));
        doc1.add(new IntPoint("num",p1.getProductNum()));
        doc2.add(new TextField("productId",p2.getProductId(),
                Field.Store.YES));
        doc2.add(new TextField("productCat",p2.getProductCategory(),
                Field.Store.YES));
        doc2.add(new StringField("productDesc",p2.getProductDescription(),
                Field.Store.YES));
        doc2.add(new TextField("productName",p2.getProductName(),
                Field.Store.NO));
        doc2.add(new StringField("num-view",p2.getProductNum()+"个", Field.Store.YES));
        doc2.add(new IntPoint("num",p2.getProductNum()));
         //将document封装到writer
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        //创建索引
        writer.commit();//文件夹出现默认空环境结构，没有数据
    }
}
