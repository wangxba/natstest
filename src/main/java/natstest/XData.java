package natstest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paratera.comutil.util.UniqUtils;

public class XData {
    private static final Log LOG = LogFactory.getLog(XData.class);
    private String cid;
    private String hid;
    private String ip;
    private String hostname;
    private String keyword;
    private String topic;
    private long time;
    private String rawData;
    private JSONObject jsonData;
    private String docId;
    private String typeId;
    private String collectKey;
    private boolean isCollectData;
    private long collectTime;
    protected DataType dataType;

    public XData() {
        dataType = new DataType();
        collectTime = 0;
    }

    public boolean isCollectData() {
        return isCollectData;
    }

    public void setCollectData(boolean isCollectData) {
        this.isCollectData = isCollectData;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCollectKey() {
        return collectKey;
    }

    public void setCollectKey(String collectKey) {
        this.collectKey = collectKey;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }
    
    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public JSONObject getJsonData() {
        if (jsonData == null && rawData != null) {
            jsonData = JSON.parseObject(rawData);
        }
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    private static final String KEYWORD_SPLIT = ".";



    public boolean isProc() {
        return dataType.checkAndSet(DataType.DATA_TYPE.PROC,getKeyword());
    }

    public boolean isSyslog() {
        return dataType.checkAndSet(DataType.DATA_TYPE.SYSLOG,getKeyword());
    }

    public boolean isJobRelated() {
        return dataType.checkAndSet(DataType.DATA_TYPE.JOB_RELATE,getKeyword());
    }

    public boolean isSwitchInfo() {
        return dataType.checkAndSet(DataType.DATA_TYPE.SWITCH, getKeyword());
    }

    /**
     * JS is short for JobSchedule
     * @return
     */
    public boolean isJSHistoryType() {
        return dataType.checkAndSet(DataType.DATA_TYPE.JS_HISTORY,getKeyword());
    }
    
    public boolean isJSNodeInfoType() {
        return dataType.checkAndSet(DataType.DATA_TYPE.JS_NODE_INFO,getKeyword());
    }
    
    public boolean isJSRunningJobType() {
        return dataType.checkAndSet(DataType.DATA_TYPE.JS_RUNNING_JOB,getKeyword());
    }
    
    public boolean isJSPartInfoType() {
        return dataType.checkAndSet(DataType.DATA_TYPE.JS_PART_INFO,getKeyword());
    }
    
    public boolean isSysinfo() {
        return dataType.checkAndSet(DataType.DATA_TYPE.SYSINFO,getKeyword());
    }
    
    public boolean isNet(){
    	return dataType.checkAndSet(DataType.DATA_TYPE.NET,getKeyword());
    }
    
    public boolean isSmart() {
        return dataType.checkAndSet(DataType.DATA_TYPE.SMART,getKeyword());
    }
    
    public String findCollectKey() {
        if (StringUtils.isBlank(collectKey)) {
            StringBuilder sb = new StringBuilder(getKeyword());
            sb.append(KEYWORD_SPLIT).append("COLLECT").append(this.hid);
            collectKey =  sb.toString();
        }
        return collectKey;
    }
    
    public String findType() {
        if (StringUtils.isBlank(typeId)) {
            int firstIndex = keyword.indexOf(KEYWORD_SPLIT);
            int secondIndex = keyword.indexOf(KEYWORD_SPLIT, firstIndex+1);
            typeId = keyword.substring(secondIndex+1);
        }
        return typeId;
    }
    
    public String findDocId() {
        if (StringUtils.isBlank(docId)) {
            StringBuilder sb = new StringBuilder();
            sb.append(hid).append(KEYWORD_SPLIT).append(time);
            if (isProc()) {
                long pid = getJsonData().getLongValue("pid");
                sb.append(KEYWORD_SPLIT).append(pid);
            } else if (isSyslog()) {
                sb.append(KEYWORD_SPLIT).append(UniqUtils.getNanoTime());
            } else if (isJobRelated()) {
                if (isJSHistoryType() || isJSRunningJobType()) {
                    sb.append(KEYWORD_SPLIT).append(getJsonData().getLongValue("jobid"));
                } else if (isJSNodeInfoType() || isJSPartInfoType()){
                    sb.append(KEYWORD_SPLIT).append(getJsonData().getString("name"));
                }
            }
            docId = DigestUtils.md5Hex(sb.toString());
        }
        return docId;
    }
    
    @Override
    public XData clone() {
        XData data = new XData();
        data.cid = this.cid;
        data.hid = this.hid;
        data.keyword = this.keyword;
        data.rawData = this.rawData;
        data.collectKey = this.collectKey;
        data.time = this.time;
        data.topic = this.topic;
        data.isCollectData = this.isCollectData;
        data.collectTime = this.collectTime;
        if (this.jsonData != null) {
            data.jsonData = (JSONObject)this.jsonData.clone();
        }
        return data;
    }
    
    public XData smartCopy() {
        XData data = new XData();
        data.cid = this.cid;
        data.hid = this.hid;
        data.keyword = this.keyword;
        data.time = this.time;
        data.collectKey = this.collectKey;
        data.topic = this.topic;
        data.isCollectData = this.isCollectData;
        data.collectTime = this.collectTime;
        return data;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("key=").append(this.keyword)
        .append(" value=").append(this.rawData);
        return sb.toString();
        
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
