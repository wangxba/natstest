/**
 * com.paratera.importdata.parser
 * MysqlWriter.java
 * 
 * 2015年6月30日
 * 2015北京并行科技公司-版权所有
 * 
 */
package natstest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.google.gxp.com.google.common.collect.Sets;
import com.paratera.comutil.ExpireMap;
import com.paratera.sqlplug.MysqlConnector;

import natstest.SqlBuilder.Schema;


/**
 * @author chenchao@paratera.com
 *
 */
public class MysqlWriter {
    public static final ExpireMap<String, List<String>> EXPIRE_MAP = new ExpireMap<String, List<String>>();
    public static final ExpireMap<String, Schema> TABLE_SCHEMA_EXPIRE_MAP = new ExpireMap<String, Schema>();
    public static final Set<String> SKIP_PARACHDB_COLUMN = Sets.newHashSet();
    static {
        SKIP_PARACHDB_COLUMN.add("id");
    }
    
    public static Schema getColumnSchema(MysqlConnector connector, String tablename) throws SQLException {
        Schema schema = TABLE_SCHEMA_EXPIRE_MAP.get(tablename);
        if (schema != null) {
            return schema;
        } 
        
        try (Connection conn = connector.getConnectionPool().getConnection(); 
                ResultSet rs = conn.getMetaData().getColumns(null, null, tablename.toUpperCase(), null);){
            schema = new Schema();
            schema.setTable(tablename.toUpperCase());
            while(rs.next()){
                String col  = rs.getString("COLUMN_NAME");
                int sqlType = rs.getInt("DATA_TYPE");
                if (SKIP_PARACHDB_COLUMN.contains(col)) {
                    continue;
                }
                schema.put(col, sqlType);
            }
        } 
        synchronized(tablename.toUpperCase().intern()) {
            if (!TABLE_SCHEMA_EXPIRE_MAP.containsKey(tablename)) {
                // valid in 2 minutes
                TABLE_SCHEMA_EXPIRE_MAP.put(tablename, schema, System.currentTimeMillis()+120000L);
            }
        }
        return schema;
    }
    
    
    private static final Set<String> INVALID_STRING = Sets.newHashSet();
    static {
        INVALID_STRING.add(Float.toString(Float.NaN));
        INVALID_STRING.add(Float.toString(Float.NEGATIVE_INFINITY));
        INVALID_STRING.add(Float.toString(Float.POSITIVE_INFINITY));
    }
        
}
