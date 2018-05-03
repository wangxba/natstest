/**
 * model
 * com.paratera.model
 * Consts.java
 * 
 * 2015年6月23日
 * 2015北京并行科技公司-版权所有
 * 
 */
package natstest;

import com.paratera.comutil.exception.AppException.ErrorCode;

public interface Consts {
    static final ErrorCode UNKNOWN = new ErrorCode() {
        @Override
        public int getCode() {
            return 15000;
        }
    };
    static final ErrorCode INPUTS_NULL = new ErrorCode() {
        @Override
        public int getCode() {
            return 15001;
        }
    };
    static final ErrorCode OUTPUTS_NULL = new ErrorCode() {
        @Override
        public int getCode() {
            return 15002;
        }
    };
    static final ErrorCode CORE_NULL = new ErrorCode() {
        @Override
        public int getCode() {
            return 15003;
        }
    };
    
    static final ErrorCode NATS_MSG_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15004;
        }
    };
    
    static final ErrorCode NOT_SUPPORT_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15005;
        }
    };
    
    static final ErrorCode INNER_SEVICE_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15006;
        }
    };
    
    static final ErrorCode UPDATE_SNAPSHOT_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15007;
        }
    };
    
    static final ErrorCode MYSQL_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15008;
        }
    };
    
    static final ErrorCode QUEUE_FULL_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15009;
        }
    };
    
    static final ErrorCode COLLECT_FLOW_NOT_MATCH_COLLECT_CORE_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15010;
        }
    };

    static final ErrorCode MYSQL_TABLE_NOT_EXIST = new ErrorCode() {
        @Override
        public int getCode() {
            return 15011;
        }
    };

    static final ErrorCode EXPLAIN_RULE_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15012;
        }
    };

    static final ErrorCode INIT_RULE_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15012;
        }
    };

    static final ErrorCode DISPATCH_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15013;
        }
    };

    static final ErrorCode OFFLINE_FLOW_NOT_MATCH_OFFLINE_CORE_ERROR = new ErrorCode() {
        @Override
        public int getCode() {
            return 15014;
        }
    };

    static final String KEY_HOST_ID = "hostid";
    static final String KEY_CLUSTER_ID = "cluster";
    static final String KEY_TIMESTAMP = "timestamp";
    static final String DATE_FORMAT_INDEX = "yyyyMMdd";
    static final String ERROR_CODE = "code";
    static final String KEY_IP="ip";
    static final String KEY_HOST_NAME="hostname";

    static final long _2_MINUTES_IN_MILLS = 2*60*1000;
}
