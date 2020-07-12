package cn.tedu.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.StringReader;

/**
 * @author Mangmang
 * @date 2020/2/23 18:43
 */
public class AnalyzerTest {
    /*
    测试方法，实现对一个字符串的使用分词器实现类解析
    的过程，不需要关心分词计算底层，只要使用实现类
    将分词结果打印出来 看到词，字，段的文字
     */
    //org.apache.lucene.*
    public void analyze(Analyzer analyzer, String msg)throws Exception{
        StringReader stringReader = new StringReader(msg);//将字符串对象，转化成流数据
        TokenStream stream = analyzer.tokenStream("test", stringReader); //使用分词器对象，获取切分后的流数据
        stream.reset(); //二进制计算完分词，已经指针指向末尾
        //查看stream中的各个切分之后的词项的文本属性打印
        //拿到文本属性
        CharTermAttribute attribute = stream.getAttribute(CharTermAttribute.class);
        //循环遍历每一个二进制，打印文本属性
        while (stream.incrementToken()) {
            System.out.println(attribute.toString());
        }
    }

    //编写测试代码，提供多个不同的实现类对象
    @Test
    public void run () throws Exception  {
        //准备一个字符串
        String msg="和妹妹打王者荣耀，中华人民共和国";
        //分词器实现类
        Analyzer a1=new StandardAnalyzer();
        Analyzer a2=new SimpleAnalyzer();
        Analyzer a3=new WhitespaceAnalyzer();
        Analyzer a4=new SmartChineseAnalyzer();
        Analyzer a5=new IKAnalyzer6x();
        System.out.println("************标准**********");
        analyze(a1,msg);
        System.out.println("************简单**********");
        analyze(a2,msg);
        System.out.println("************空格**********");
        analyze(a3,msg);
        System.out.println("************智能**********");
        analyze(a4,msg);
        System.out.println("************IK**********");
        analyze(a5,msg);

    }
}


