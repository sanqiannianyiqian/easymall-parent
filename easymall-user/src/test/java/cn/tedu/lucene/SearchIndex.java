package cn.tedu.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Mangmang
 * @date 2020/2/24 21:18
 */
public class SearchIndex {
    /*
     词项查询 对域名，词项值匹配，一旦匹配返回查询结果
      */
    @Test
    public void termQuery() throws Exception {

        Path path = Paths.get("D:/index01");  //获取索引文件通道,指向索引文件
        FSDirectory dir = FSDirectory.open(path);
        IndexReader reader  = DirectoryReader.open(dir);//获取输入流，构造一个搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        Term term=new Term("productName","手机");//创建一个搜索对象 TermQuery
        Query query = new TermQuery(term);
        //浅查询遍历数据结果
        //拿到对标识做了计算的一个数据 topDocs
        TopDocs topDocs = searcher.search(query, 10);//拿到前10条数据
        // doc id计算结果 里面只封装了 查询评分和docid
        //从中解析封装docId的对象数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {   //数组每个元素就是docId封装
            System.out.println("docId:"+scoreDoc.doc);     //解析需要搜索的分页的id值
            Document doc = searcher.doc(scoreDoc.doc);   //通过id读取document对象
            System.out.println("productName" +doc.get("productName")); //使用数据，打印数据值
            System.out.println("productCat" +doc.get("productCat"));
            System.out.println("productDesc" +doc.get("productDesc"));
            System.out.println("productNum" +doc.get("num"));
        }
    }
    @Test
    public void multiFieldQuery() throws Exception {
        //指向搜索数据索引文件
        Path path = Paths.get("d:/index01");
        FSDirectory dir=FSDirectory.open(path);
        //拿到搜索对象
        IndexReader reader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(reader);
        //构造查询条件
        //多个域
        String[] fields={"productName","productCat","productDesc"};
        //解析查询文本的解析器 需要使用分词器
        MultiFieldQueryParser parser=new MultiFieldQueryParser(fields,new IKAnalyzer6x());
        //当使用一个字符串进行查询时，先进行分词计算
            /*
            "达内手机顶呱呱"-->手机，顶呱呱
            排列组合
            proudctName:手机，productName:顶呱呱
            productCat:手机，productCat:顶呱呱
            productDesc:手机，productDesc:顶呱呱
            一旦任何一个词项匹配到了document，就将其放到
            返回结果中使用             */
        //提供一个查询关键字文字
        String text="达内手机顶呱呱";
        Query query = parser.parse(text);//向上造型多域查询
        //遍历循环。
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            System.out.println("documentId:"+scoreDoc.doc);
            //获取document对象
            Document doc = searcher.doc(scoreDoc.doc);
            //使用数据，打印数据值
            System.out.println("productName"
                    +doc.get("productName"));
            System.out.println("productCat"
                    +doc.get("productCat"));
            System.out.println("productDesc"
                    +doc.get("productDesc"));
            System.out.println("productNum"
                    +doc.get("num"));
        }
    }
    @Test
    public void booleanQuery() throws Exception {
        Path path = Paths.get("d:/index01");
        FSDirectory dir=FSDirectory.open(path);
        IndexReader reader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(reader);
        //构造查询条件
        //多个布尔子条件查询
        Query query1=new TermQuery(new Term("productName","手机"));
        Query query2=new TermQuery(new Term("productCat","手机"));
        //子查询封装成布尔的子条件
        //Occur发生逻辑值
        //must:查询结果一定属于该子条件的一部分
        //must_not：查询结果一定不属于该子条件
        //should：查询结果可以包含，也可以不包含该解条件，MUST同时使用
        //以must为准
        //filter：must相同的效果，
        // 但是使用filter的条件对应的结果，没有评分

        BooleanClause bc1=new BooleanClause(query1,
                BooleanClause.Occur.MUST);
        BooleanClause bc2=new BooleanClause(query2,
                BooleanClause.Occur.MUST_NOT);
        //利用2个子条件封装布尔
        Query query=new BooleanQuery.Builder().add(bc1)
                .add(bc2).build();
        //遍历循环。
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            System.out.println("documentId:"+scoreDoc.doc);
            //获取document对象
            Document doc = searcher.doc(scoreDoc.doc);
            //使用数据，打印数据值
            System.out.println("productName"
                    +doc.get("productName"));
            System.out.println("productCat"
                    +doc.get("productCat"));
            System.out.println("productDesc"
                    +doc.get("productDesc"));
            System.out.println("productNum"
                    +doc.get("num"));
        }
    }
    /*
    数字范围搜索
     */
    @Test
    public void rangeQuery() throws Exception {
        Path path = Paths.get("d:/index01");
        FSDirectory dir=FSDirectory.open(path);
        IndexReader reader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(reader);
        //构造查询条件
        //使用num域实现范围查询
        Query query= IntPoint.newRangeQuery("num",100,300);
        //遍历循环。
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc:scoreDocs){
            System.out.println("documentId:"+scoreDoc.doc);
            //获取document对象
            Document doc = searcher.doc(scoreDoc.doc);
            //使用数据，打印数据值
            System.out.println("productName"
                    +doc.get("productName"));
            System.out.println("productCat"
                    +doc.get("productCat"));
            System.out.println("productDesc"
                    +doc.get("productDesc"));
            System.out.println("productNum"
                    +doc.get("num"));
        }
    }

}
