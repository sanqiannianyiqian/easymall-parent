package cn.tedu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.pojo.Product;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Mangmang
 * @date 2020/2/26 11:52
 */
public class ESTest {

    //@Before将一个连接对象实现初始化，所有Test
    //代码就会before不需要每个方法都重新创建一遍
    private TransportClient transportClient;

    //对client初始化
    @Before
    public void initClient() throws UnknownHostException {
        //Settings.EMPTY就是连接elasticsearch这个集群
        /*Settings setting = Settings.builder(). put("cluster.name", "elasticsearch").build();*/

        transportClient = new PreBuiltTransportClient(Settings.EMPTY);
        //配置连接哪个集群名称,Settings.EMPTY默认连接elasticsearch,否则按上面写填入setting

        //连接使用的节点ip和端口
        //将负载均衡节点 3个都是，交给client
        //node1
        InetSocketTransportAddress address1 = new InetSocketTransportAddress(InetAddress.getByName("10.42.76.141"), 9300);
        //node2
        InetSocketTransportAddress address2 = new InetSocketTransportAddress(InetAddress.getByName("10.42.37.248"), 9300);
        //node3
        InetSocketTransportAddress address3 = new InetSocketTransportAddress(InetAddress.getByName("10.42.20.206"), 9300);

        //三个address交给client,从三个拿一个可以连接的,一个也行,为了高可用
        transportClient.addTransportAddresses(address1, address2, address3);

    }

    /*
  索引管理，增加索引，判断存在，删除等
*/
    @Test
    public void indexManage() {
        //通过client拿到索引管理对象
        AdminClient admin = transportClient.admin();
        IndicesAdminClient indices = admin.indices();
        //TransportClient中有2套方法，一套直接发送调用

        //一套是预先获取request对象。下面用的是这种因为结构比较明白
        CreateIndexRequestBuilder request1 = indices.prepareCreate("index01");//不存在的索引名称
        IndicesExistsRequestBuilder request2 = indices.prepareExists("index01");
        if (request2.get().isExists()) {
            System.out.println("index01已经存在");
//            indices.prepareDelete("index01").get();//建立
            return;
        }

        //发送请求request1到es
        CreateIndexResponse response1 = request1.get();
        //从reponse中解析一些有需要的数据
        System.out.println(response1.isShardsAcked());//json一部分 shards_acknowleged:true
        System.out.println(response1.remoteAddress());
        response1.isAcknowledged();//json acknowledged:true
    }

    ;

    /*
   文档管理：新建，删除，获取 indexName typeName docid
    */
    @Test
    public void panduan() {
        System.out.println(indexExist("index01"));
    }

    //方法的二次封装,高级架构师或者高级工程师会这么做,封装复杂的API
    public boolean indexExist(String indexName) {
        //通过client拿到索引管理对象
        AdminClient admin = transportClient.admin();
        // ClusterAdminClient cluster = admin.cluster();
        IndicesAdminClient indices = admin.indices();
        return indices.prepareExists(indexName).get().isExists();
    }

    @Test
    public void documentManage() throws JsonProcessingException {
        //新增文档，准备文档数据
        //拼接字符串 对象与json的转化工具ObjectMapper
        Product p = new Product();
        p.setProductNum(500);
        p.setProductName("三星手机");
        p.setProductCategory("手机");
        p.setProductDescription("能攻能守，还能爆炸");
        String pJson = new ObjectMapper().writeValueAsString(p);
        //client 获取request发送获取response 不是真正的httprequest
        //curl -XPUT -d {json} http://10.9.104.184:9200/index01/product/1
        IndexRequestBuilder request = transportClient.prepareIndex("index01", "product", "1");
        //source填写成pJson
        request.setSource(pJson);
        request.get();
    }

    @Test
    public void getDocument() throws IOException {
        //获取一个document只需要index type id坐标
        GetRequestBuilder request = transportClient.prepareGet("index01", "product", "1");
        GetResponse response = request.get();
        //从response中解析数据
        System.out.println(response.getIndex());
        System.out.println(response.getId());
        System.out.println(response.getVersion());
        System.out.println(response.getSourceAsString());
        Product product = new ObjectMapper().readValue(response.getSourceAsString(), Product.class);
        System.out.println(product);
//        transportClient.prepareDelete("index01", "product", "1").get();//删除
//        transportClient.prepareDelete("index01", "product", "product.getProductId()").get();//商品业务层删除
    }
    /*
        搜索功能使用
         */
    @Test
    public void search() {
        //封装查询条件，不同逻辑查询条件对象不同
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("productName", "星");//productName中有星的
        //底层使用lucene查询，基于浅查询，可以支持分页
        SearchRequestBuilder request = transportClient.prepareSearch("index01");
        //在request中封装查询条件，分页条件等
        request.setQuery(termQueryBuilder);
        request.setFrom(0);//起始位置 类似limit start
        request.setSize(5);//
        SearchResponse response = request.get();
        //从response对象中解析搜索的结果
        //解析第一层hits相当于topDocs
        SearchHits searchHits = response.getHits();
        System.out.println("总共查到："+searchHits.totalHits);
        System.out.println("最大评分："+searchHits.getMaxScore());//评分越高匹配度越高
        SearchHit[] hits = searchHits.getHits();//循环遍历的结果
        for (SearchHit hit : hits) {     //hits包含想要的查询结果
            System.out.println(hit.getSourceAsString());
        }
    }
}
