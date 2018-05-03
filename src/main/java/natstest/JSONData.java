/**
 * com.paratera.importdata.parser93.mysql.data
 * JSONData.java
 * 
 * 2015年11月6日
 * 2015北京并行科技公司-版权所有
 * 
 */
package natstest;

import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

/**
 * @author chenchao@paratera.com
 *
 */
public class JSONData  {
    private String distributeKey;
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getDistributeKey() {
        return distributeKey;
    }

    public void setDistributeKey(String distributeKey) {
        this.distributeKey = distributeKey;
    }
    private String tableName;
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JSONObject data;
    

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        ToStringHelper helper = MoreObjects.toStringHelper(this)
            .add("tableName", tableName);
        for (Entry<String,Object> entry : data.entrySet()) {
            helper.add(entry.getKey(), entry.getValue());
        }
        return helper.toString();
    }

    
}
