/**
 * com.paratera.importdata.parser93.mysql
 * SqlBuilder.java
 * 
 * 2015年11月10日
 * 2015北京并行科技公司-版权所有
 * 
 */
package natstest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paratera.comutil.exception.AppRuntimeException;
import com.paratera.comutil.util.DaoUtils;
import com.paratera.sqlplug.MysqlConnector;

/**
 * @author chenchao@paratera.com
 *
 */
public class SqlBuilder {
    private MysqlConnector conn;
    public SqlBuilder(MysqlConnector conn) {
        this.conn = conn;
    }
    
    public static final Map<String, String> columnMap = Maps.newHashMap();
    public static final Map<String, String> newColumn = Maps.newHashMap();
    static{
    	columnMap.put("idle.avg", "cpu");
        columnMap.put("iowait.avg", "cpu");
        columnMap.put("sys.avg", "cpu");
        columnMap.put("user.avg", "cpu");
        columnMap.put("nice.avg", "cpu");
        columnMap.put("irq.avg", "cpu");
        columnMap.put("softirq.avg", "cpu");
        columnMap.put("now.avg", "power");
        columnMap.put("system_temp.avg", "sensors");
        columnMap.put("disk_all_read.avg", "dskio");
        columnMap.put("disk_all_write.avg", "dskio");
        columnMap.put("disk_all_tps.avg", "dskio");        
        columnMap.put("gpu_utilization.avg", "coprocessor");        
        columnMap.put("mem_utilization.avg", "coprocessor");        
        columnMap.put("gflops.avg", "coprocessor");        
        columnMap.put("running_proc_num.avg", "coprocessor");
        columnMap.put("disk_all_tps.avg", "dskio");
        columnMap.put("gpu_utilization.avg", "coprocessor");
        columnMap.put("rcv_bytes.avg", "if");
        columnMap.put("snd_bytes.avg", "if");
        columnMap.put("readbytes.avg", "nfsd");
        columnMap.put("writebytes.avg", "nfsd");
        columnMap.put("space.avg", "df");
        columnMap.put("free.avg", "df");
        columnMap.put("port_rcv_remote_physical_errors.avg", "ib");

        columnMap.put("inst_min.min", "inst");
        columnMap.put("inst_max.max", "inst");
        columnMap.put("inst_avg.avg", "inst");
        columnMap.put("l1i_miss_min.min", "l1i_miss");
        columnMap.put("l1i_miss_max.max", "l1i_miss");
        columnMap.put("l1i_miss_avg.avg", "l1i_miss");
        columnMap.put("gld_req_min.min", "gld_req");
        columnMap.put("gld_req_max.max", "gld_req");
        columnMap.put("gld_req_avg.avg", "gld_req");

        

        newColumn.put("cid", "cluster");
        newColumn.put("hid", "hostid");
    }
    
    public static class Schema {
        private String table;
        private Map<String,Integer> columns;
        private String columnSQLPart;
        
        public Map<String, Integer> getColumns() {
            return columns;
        }
        public void setColumns(Map<String, Integer> columns) {
            this.columns = columns;
        }
        public String getTable() {
            return table;
        }
        public String getColumnSQLPart() {
            if (columns == null) {
                return null;
            } else if (columnSQLPart == null) {
                columnSQLPart = new StringBuilder().append("`").append(StringUtils.join(this.columns.keySet(), "`,`")).append("`").toString();
            }
            return columnSQLPart;
        }
        public void setTable(String table) {
            this.table = table;
        }
        public Schema() {
            columns = new HashMap<String, Integer>();
        }
        public void put(String column , int colType) {
            columns.put(column, colType);
        }
    }

    public List<Object> getSQLVal(JSONData data, Schema schema) {
        List<Object> list = Lists.newArrayList();
        for (Entry<String, Integer> entry : schema.getColumns().entrySet()) {
            String name = entry.getKey();
            Integer type = entry.getValue();
            list.add(getDataVal(data, name, type));
        }
        return list;
    }
    
    public String createSQL(String sqlFormat, JSONData data) throws SQLException {
        Schema schema = getSchema(data);
        String table = schema.getTable();
        String columns = schema.getColumnSQLPart();
        List<Object> valList = getSQLVal(data, schema);
        String values = StringUtils.join(valList, ",");
        return String.format(sqlFormat, new StringBuilder().append("`").append(table).append("`").toString(), columns, values);
    }

    public String createReplaceSQL(JSONData data) throws SQLException {
        //replace into JOBACCT(`sub_time`,`start_time`,`cluster_id`,`cpu_time`,`slots`,`job_id`,`user_name`,`end_time`,`wall_time`,`updated`,`node_id`) values ('1970-01-01 08:03:00.0','1970-01-01 08:04:00.0',208,60.0,2,'job_id2','user_name2','1970-01-01 08:05:00.0',120.0,'2015-11-30 09:13:28.019',19141)
        String sqlFormat = "replace into %s(%s) values (%s)";
        return createSQL(sqlFormat, data);
    }

    public String createInsertSQL(JSONData data) throws SQLException {
        //insert into dfstat(cluster_id, node_id, source, fstype, target, size, used, avail, pcent, updated) values(?,?,?,?,?,?,?,?,?,?)
        String sqlFormat = "insert into %s(%s) values (%s)";
        return createSQL(sqlFormat, data);
    }
    private static final String NULL_STR = "null";
    private Object getDataVal(JSONData data, String typeName, Integer type)  {
        JSONObject json = data.getData();
        if(columnMap.containsKey(typeName)){
            json = json.getJSONObject(columnMap.get(typeName));
        }
        
        if(newColumn.containsKey(typeName)){
            typeName = newColumn.get(typeName);
        }
        if(typeName.equals("created_date")){
            Timestamp timestamp = DaoUtils.now();
            timestamp.setSeconds(0);
            timestamp.setNanos(0);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("'").append(timestamp).append("'");
            return stringBuilder.toString();
        }
        if(typeName.equals("json")){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("'").append(json).append("'");
            return stringBuilder.toString();
        }
        typeName = typeName.replace(".avg", "");
        if (json == null||!json.containsKey(typeName)) {
            return NULL_STR;
        }

        switch (type) {
            case Types.BOOLEAN:
                return json.getBoolean(typeName);
            case Types.FLOAT:
                return json.getFloat(typeName);
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.REAL:
            case Types.DOUBLE:
                return json.getDouble(typeName);
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.BIGINT:
            case Types.SMALLINT:
                return json.getLong(typeName);
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                // FIXME
                Timestamp date = json.getTimestamp(typeName);
                if (date == null) {
                    return NULL_STR;
                }
                StringBuilder datesb = new StringBuilder();
                datesb.append("'").append(date).append("'");
                return datesb.toString();
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.CLOB:
            case Types.NCHAR:
            case Types.NCLOB:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                String val = json.getString(typeName);
                if (val == null) {
                    return NULL_STR;
                }
                StringBuilder charsb = new StringBuilder();
                charsb.append("'").append(val).append("'");
                return charsb.toString();
            default:
                throw new AppRuntimeException(Consts.NOT_SUPPORT_ERROR,"not support data:%s typeName:%s type:%s",data, typeName, type);
        }
    }

    private Schema getSchema(JSONData data) throws SQLException {
        Schema schema = MysqlWriter.getColumnSchema(conn, data.getTableName());
        if (schema == null) {
            throw new AppRuntimeException(Consts.MYSQL_TABLE_NOT_EXIST,"not support. no schema. data:%s",data);
        }
        return schema;
    }
}
