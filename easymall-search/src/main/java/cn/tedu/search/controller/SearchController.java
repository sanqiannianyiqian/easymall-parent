package cn.tedu.search.controller;

import cn.tedu.search.service.SearchService;
import com.jt.common.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/27 20:41
 */
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
@RequestMapping("/search/manage/query")
    public List<Product> searchProducts(String query, Integer page, Integer rows) {
        return searchService.search(query, page, rows);
    }
}
