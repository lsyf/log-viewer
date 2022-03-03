package com.lsyf.pay.logger.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Segment {
    String traceId;
    String segmentId;
    String pSegmentId;
    String pServiceCode;
    String serviceCode; //服务名
    long startTime;
    long endTime;
    boolean error; //是否异常
    String refType;


}