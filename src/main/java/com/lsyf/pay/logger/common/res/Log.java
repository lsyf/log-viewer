package com.lsyf.pay.logger.common.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    String traceId;
    String sId;
    String pId;
    String serviceCode; //服务名
    String time;
    boolean error; //是否异常
    String level; //日志级别

    String source;

    public void clear() {
        this.traceId = null;
        this.pId = null;
        this.serviceCode = null;
        this.time = null;
    }
}