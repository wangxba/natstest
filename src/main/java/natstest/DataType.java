package natstest;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by chico on 16/11/30.
 */
public class DataType {

    public enum DATA_TYPE {
        SYSLOG((str)->{return str.contains(".syslog.");}),
        PROC((str)->{return str.contains(".proc.");}),
        JOB_RELATE((str)->{return str.matches("\\w+.js\\w+.\\w+");}),
        JS_HISTORY((str)->{return str.endsWith(".history");}),
        JS_NODE_INFO((str)->{return str.endsWith(".nodesinfo");}),
        JS_RUNNING_JOB((str)->{return str.endsWith(".running");}),
        JS_PART_INFO(str->{return str.endsWith(".parts");}),
        SYSINFO(str->{return str.endsWith(".sysinfo");}),
        SMART((str)->{return str.endsWith(".smartd");}),
        SWITCH((str)->{return str.endsWith(".switch");}),
    	NET((str)->{return str.endsWith(".net");});
        private Function<String, Boolean> fun;

        public Function<String, Boolean> getFun() {
            return fun;
        }

        private DATA_TYPE(Function<String, Boolean> fun) {
            this.fun  =fun;
        }
    }

    public DataType() {
        typeMap = Maps.newHashMap();
    }
    private Map<DATA_TYPE,Boolean> typeMap;
    public boolean checkAndSet(DATA_TYPE type, String str) {
        if (typeMap.containsKey(type)) {
            return typeMap.get(type);
        }
        boolean ret = type.getFun().apply(str);
        typeMap.put(type, ret);
        return ret;
    }

}
