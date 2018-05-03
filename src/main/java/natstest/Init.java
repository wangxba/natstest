package natstest;

import com.paratera.sqlplug.jdbc.Store;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
/*
 * Created by zhangjs on 3/14/17.
 */
@Configuration
public class Init{


    @Bean(name = "jdbc")
    public NamedParameterJdbcTemplate getJdbc() {
        return new Store().getJdbc();
    }
    
    @Bean(name = "mysqlManager")
    public MysqlManager getMysqlManager() {
        return new MysqlManager();
    }
}
