package com.lsyf.pay.logger.common.res;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppLog {
    String traceId;
    String segmentId;
    String pSegmentId;
    String serviceCode; //服务名
    String pServiceCode;//调用方服务名
    String startTime;
    String endTime;
    Long costTime;
    Integer lvl;
    boolean error = false; //是否异常
    boolean newThread = false; //是否线程调用

    List<Log> logs = Lists.newArrayList();


}