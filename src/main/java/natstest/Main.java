/**
 * 
 */
package natstest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springside.modules.utils.collection.QueueUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

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
    
    
    private static List<String> z800_cache = Lists.newArrayList();
    
    private static List<String> z820_cache = Lists.newArrayList();
    
    private static List<String> z840_cache = Lists.newArrayList();
    
    private static List<String> server_cache = Lists.newArrayList();
    
    private static List<String> ibm512_cache = Lists.newArrayList();
    
    private static List<String> hp128_cache = Lists.newArrayList();
    
//    dqhm   dqlm   gpu    sg
    private static List<String> dqhm_cache = Lists.newArrayList();
    private static List<String> dqlm_cache = Lists.newArrayList();
    private static List<String> gpu_cache = Lists.newArrayList();
    private static List<String> sg_cache = Lists.newArrayList();
    private static List<String> xw9400_cache = Lists.newArrayList();
    
    
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
//        SpringApplication.run(Main.class, args);
//        MysqlManager manager = ApplicationContextUtils.getBean(MysqlManager.class);
//        MysqlConnector conn = manager.getInstance("jdbc.properties");
//        JdbcTemplate jdbc = conn.getJdbcTemplate();
//        File file = new File("nats.txt");
//        Nats nats = new NatsConnector().addHost("nats://127.0.0.1:4242").connect();
//
    	  Nats nats = new NatsConnector().addHost("nats://159.10.133.134:4242").connect();
    	  
//    	  nats.publish("test.cmd","eval ps -ef|grep /opt/hpcoder/pgatherd/etc/vncctrl/scripts/tmp/gaoqi/20190424102017 ");
//    	  Subscription subscription1 = nats.subscribe("test.cmd");
//    	  subscription1.addMessageHandler(new MessageHandler() {
//              public void onMessage(Message message) {
//              	System.out.println(message);
//              }
//          });
          Subscription hp128 = nats.subscribe("R.hp128.sysinfo.sysinfo");
          Subscription ibm512 = nats.subscribe("R.ibm512.sysinfo.sysinfo");
          Subscription z800 = nats.subscribe("R.z800.sysinfo.sysinfo");
          Subscription z820 = nats.subscribe("R.z820.sysinfo.sysinfo");
          Subscription z840 = nats.subscribe("R.z840.sysinfo.sysinfo");
          Subscription server = nats.subscribe("R.server.sysinfo.sysinfo");
          Subscription dqhm = nats.subscribe("R.dqhm.sysinfo.sysinfo");
          Subscription dqlm = nats.subscribe("R.dqlm.sysinfo.sysinfo");
          Subscription sg = nats.subscribe("R.sg.sysinfo.sysinfo");
          Subscription gpu = nats.subscribe("R.gpu.sysinfo.sysinfo");
          Subscription xw9400 = nats.subscribe("R.xw9400.sysinfo.sysinfo");
        // 多个消息处理器

//         Multiple message handlers
        hp128.addMessageHandler(new MessageHandler() {
            public void onMessage(Message message) {
            	try {
            	    XData data = subscribeSysInfo(message);
                    if(data!=null) {
                        if(!hp128_cache.contains(data.getHostname())) {
                            hp128_cache.add(data.getHostname());
                            FileUtils.write(new File("/opt/hp128.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
          dqhm.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!dqhm_cache.contains(data.getHostname())) {
                              dqhm_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/dqhm.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          dqlm.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!dqlm_cache.contains(data.getHostname())) {
                              dqlm_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/dqlm.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          sg.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!sg_cache.contains(data.getHostname())) {
                              sg_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/sg.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          gpu.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!gpu_cache.contains(data.getHostname())) {
                              gpu_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/gpu.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          ibm512.addMessageHandler(new MessageHandler() {
            public void onMessage(Message message) {
                try {
                    XData data = subscribeSysInfo(message);
                    if(data!=null) {
                        if(!ibm512_cache.contains(data.getHostname())) {
                            ibm512_cache.add(data.getHostname());
                            FileUtils.write(new File("/opt/ibm512.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
          
          z800.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!z800_cache.contains(data.getHostname())) {
                              z800_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/z800.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                      
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          
          z820.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!z820_cache.contains(data.getHostname())) {
                              z820_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/z820.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          
          z840.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!z840_cache.contains(data.getHostname())) {
                              z840_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/z840.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          });
          
          server.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!server_cache.contains(data.getHostname())) {
                              server_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/server.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
//                      FileUtils.write(new File("/opt/server_raw.txt"), message.getBody()+"\n", "utf-8", true);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          });
          
          xw9400.addMessageHandler(new MessageHandler() {
              public void onMessage(Message message) {
                  try {
                      XData data = subscribeSysInfo(message);
                      if(data!=null) {
                          if(!xw9400_cache.contains(data.getHostname())) {
                              xw9400_cache.add(data.getHostname());
                              FileUtils.write(new File("/opt/xw9400.txt"), data.getHostname()+" "+data.getIp()+"\n", "utf-8", true);
                          }
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          });
        
     // 下面的程序主要是为了不让程序退出，不退出才能收到消息
        // 可以有如下三种方式不让程序退出
        // Block until a message arrives (message handlers still get called)
//        MessageIterator iterator = subscription.iterator();
//        Message message = iterator.next();
    }
    
    public static XData subscribeSysInfo(Message message) {
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
                return null;
            }
            String cid = jsonMsg.getString(Consts.KEY_CLUSTER_ID);
            String hid = jsonMsg.getString(Consts.KEY_HOST_ID);
            if (StringUtils.isBlank(cid) || StringUtils.isBlank(hid)) {
                return null;
            }

            long timestamp = 0;
            try {
                timestamp = jsonMsg.getLongValue(Consts.KEY_TIMESTAMP);
            } catch (Exception e) {
                return null;
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
            data.setHostname(jsonMsg.getString("hostname"));
            data.setIp(jsonMsg.getString("ip"));
//            sendNodeInfo(data,jdbc);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
