/**
 * 
 */
package natstest;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gxp.com.google.common.collect.Lists;
import com.paratera.sqlplug.MysqlConnector;
import com.paratera.sqlplug.jdbc.Store;

import nats.client.Message;
import nats.client.MessageHandler;
import nats.client.MessageIterator;
import nats.client.Nats;
import nats.client.NatsConnector;
import nats.client.Subscription;

/**
 * @author wangxb
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Main {
    
    
    private static List<String> topics = Lists.newArrayList();
    
    static {
        //cpu,gpu,meminfo,dskio,nfs,net,ipmi
        topics.add("cpu");
        topics.add("gpu");
        topics.add("meminfo");
        topics.add("dskio");
        topics.add("nfs");
        topics.add("net");
        topics.add("ipmi");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        MysqlManager manager = ApplicationContextUtils.getBean(MysqlManager.class);
        MysqlConnector conn = manager.getInstance("jdbc.properties");
        JdbcTemplate jdbc = conn.getJdbcTemplate();
        File file = new File("nats.txt");
        Nats nats = new NatsConnector().addHost("nats://127.0.0.1:4242").connect();

        Subscription subscription = nats.subscribe("5000.sysinfo.sysinfo");

        // 多个消息处理器

        // Multiple message handlers
        subscription.addMessageHandler(new MessageHandler() {
            public void onMessage(Message message) {
                try {
                    subscribeSysInfo(message,jdbc);
                    FileUtils.writeStringToFile(file, new String(message.getBody())+"\n","utf-8",true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        
     // 下面的程序主要是为了不让程序退出，不退出才能收到消息
        // 可以有如下三种方式不让程序退出
        // Block until a message arrives (message handlers still get called)
        MessageIterator iterator = subscription.iterator();
        Message message = iterator.next();
    }
    
    public static void subscribeSysInfo(Message message, JdbcTemplate jdbc) {
        try {
            String str = message.getBody(); 
            String key = message.getSubject();
            int start = str.indexOf("{");
            int end = str.lastIndexOf("}");

            str = str.substring(start, end + 1); // if the data is not start
                                                    // with {
            JSONObject jsonMsg = null;
            try {
                jsonMsg = JSON.parseObject(str);
            } catch (Exception e) {
                return;
            }
            String cid = jsonMsg.getString(Consts.KEY_CLUSTER_ID);
            String hid = jsonMsg.getString(Consts.KEY_HOST_ID);
            if (StringUtils.isBlank(cid) || StringUtils.isBlank(hid)) {
                return;
            }

            long timestamp = 0;
            try {
                timestamp = jsonMsg.getLongValue(Consts.KEY_TIMESTAMP);
            } catch (Exception e) {
                return;
            }
            XData data = new XData();
            data.setCid(cid);
            data.setHid(hid);
            data.setTime(timestamp);
            data.setKeyword(key);
            data.setTopic(key);
            data.setRawData(str);
            data.setJsonData(jsonMsg);
            data.setCollectData(false);
            data.findDocId();
            data.findType();
            data.findCollectKey();

            sendNodeInfo(data,jdbc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendNodeInfo(XData data, JdbcTemplate jdbc){
        JSONArray arr = new JSONArray();
        
        JSONObject obj = data.getJsonData();
        JSONObject tmp = (JSONObject) obj.clone();
        tmp.put("topic", data.getTopic());
        arr.add(tmp);
       
        String json = JSON.toJSONString(arr);
//        Map<String, Object> params = Maps.newHashMap();
//        params.put("body", json);
        jdbc.update("insert into t_sysinfo_raw_tab(body,addDateTime) values('"+json+"',NOW())");
 }
    
    @Bean(name = "jdbc")
    public NamedParameterJdbcTemplate getJdbc() {
        return new Store().getJdbc();
    }
    
    @Bean(name = "mysqlManager")
    public MysqlManager getMysqlManager() {
        return new MysqlManager();
    }
    
    @Bean
    public ApplicationContextUtils getContext() {
        return new ApplicationContextUtils();
    }

}
