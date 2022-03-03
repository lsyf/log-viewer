package com.lsyf.pay.logger.service.res;


import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 *
 * @Author liufei
 * @Date 2019/11/20
 */


@lombok.Data
@NoArgsConstructor
public class SWRes {

    Data data;

    public List<Span> getSpans() {
        return data == null || data.trace == null ?
                null : data.trace.spans;
    }

    @NoArgsConstructor
    @lombok.Data
    public static class Data {
        Trace trace;
    }

    @NoArgsConstructor
    @lombok.Data
    public static class Trace {
        List<Span> spans;
    }

    @NoArgsConstructor
    @lombok.Data
    public static class Span {
        /**
         * traceId : 7.90.15743874384260001
         * segmentId : 7.90.15743874384240000
         * spanId : 0
         * parentSpanId : -1
         * serviceCode : dubbo2
         * startTime : 1574387438777
         * endTime : 1574387458848
         * layer : Http
         * type : Entry
         * isError : false
         * refs : []
         */

        String traceId;
        String segmentId;
        int spanId;
        int parentSpanId;
        String serviceCode;
        long startTime;
        long endTime;
        String layer;
        String type;
        boolean isError;
        List<SpanRef> refs;
    }




    @NoArgsConstructor
    @lombok.Data
    public static class SpanRef {

        /**
         * traceId : 7.90.15743874384260001
         * parentSegmentId : 7.90.15743874384240000
         * parentSpanId : 0
         * type : CROSS_THREAD
         */

        String traceId;
        String parentSegmentId;
        int parentSpanId;
        String type;
    }

}
