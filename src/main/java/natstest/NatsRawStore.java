package natstest;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/*
 * Created by zhangjs on 2/27/17.
 */
@Repository
@Transactional
public class NatsRawStore {
    private NamedParameterJdbcTemplate jdbc = JdbcConnector.getStaticJdbc();

    
    
}
