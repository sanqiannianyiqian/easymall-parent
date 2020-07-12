package cn.tedu.search.service;

import com.jt.common.pojo.Product;
import com.jt.common.utils.MapperUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/27 20:42
 */
@Service
public class SearchService {
@Autowired
    private TransportClient client;

    public List<Product> search(String text, Integer page, Integer rows) {
        //搜索功能 封装查询条件query matchQuery
        /**match query搜索的时候，首先会解析查询字符串，进行分词，然后查询，
        // 而term query,输入的查询内容是什么，就会按照什么去查询，并不会解析查询内容，对它分词。
        // 因此创建索引时使用标准分词器也可以在这里查到**/
        MatchQueryBuilder query = QueryBuilders.matchQuery("productName", text);
        SearchRequestBuilder request = client.prepareSearch("easymallindex");
        request.setQuery(query).setFrom((page - 1) * rows).setSize(rows);
        SearchResponse searchResponse = request.get();
        //解析数据 获取第二层的hits对象
        SearchHits topHits = searchResponse.getHits();
        SearchHit[] hits = topHits.getHits();
        try {
            List<Product> products = new ArrayList<>();
            for (SearchHit hit : hits) {
                String pJson = hit.getSourceAsString();
                Product p = MapperUtil.MP.readValue(pJson, Product.class);
                products.add(p);
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
