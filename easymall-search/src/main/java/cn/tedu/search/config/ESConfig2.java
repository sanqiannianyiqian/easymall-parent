package cn.tedu.search.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.List;

/**
 * 是一个配置类，读取属性，初始化封装一个
 * transportclient对象
 */
@Configuration
@ConfigurationProperties("es")
public class ESConfig2 {
    //application.properties准备属性
    private List<String> nodes;
    @Bean
    public TransportClient initTransportClient(){
        //初始化一个Transportclient对象
        try{
            TransportClient client=new PreBuiltTransportClient
                    (Settings.EMPTY);
            for(String node:nodes){
                //node解析ip地址和端口
                String host=node.split(":")[0];
                int port=Integer.parseInt(node.split(":")[1]);
                //inetSocketTransportAddress封装使用ip port
                InetSocketTransportAddress address=new InetSocketTransportAddress(
                        InetAddress.getByName(host),port);
                client.addTransportAddress(address);
            }
            return client;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }
}
