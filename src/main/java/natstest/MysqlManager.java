package natstest;

import com.google.gxp.com.google.common.collect.Maps;
import com.paratera.comutil.CommonUtils;
import com.paratera.sqlplug.MysqlConnector;

import java.util.Map;

public class MysqlManager implements AutoCloseable {
    private final Map<String,MysqlConnector> map ;
    public MysqlManager() {
        map = Maps.newHashMap();
    }
    
    public MysqlConnector getInstance(String configFile) {
        if (map.containsKey(configFile)) {
            return map.get(configFile);
        }
        synchronized (map) {
            if (map.containsKey(configFile)) {
                return map.get(configFile);
            }
            MysqlConnector conn = new MysqlConnector(-1, configFile);
            map.put(configFile, conn);
            return conn;
        }
    }
    
    public void close() {
        synchronized (map) {
            for(MysqlConnector conn : map.values()) {
                CommonUtils.closeQuite(conn);
            }
        }
    }
}
