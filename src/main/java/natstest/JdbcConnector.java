package natstest;

import com.paratera.sqlplug.jdbc.Store;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author zhangjs
 **/
public class JdbcConnector extends Store{
    private static volatile JdbcConnector instance;

    private static JdbcConnector getInstance() {
        if (instance == null) {
            synchronized (JdbcConnector.class) {
                if (instance == null) {
                    instance = new JdbcConnector();
                }
            }
        }
        return instance;
    }


    public static NamedParameterJdbcTemplate getStaticJdbc() {
        return getInstance().jdbc;
    }
}
