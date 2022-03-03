package com.lsyf.pay.logger.common.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

public class Log4j2Util {

    public static void setID(String ID) {
        ID = StringUtils.isBlank(ID) ? String.valueOf(SnowFlake.nextId()) : ID;
        ThreadContext.put("ID", ID);
    }

    public static void setID() {
        if (ThreadContext.get("ID") == null) {
            ThreadContext.put("ID", String.valueOf(SnowFlake.nextId()));
        }
    }

    public static void clear() {
        ThreadContext.clearMap();
    }
}
