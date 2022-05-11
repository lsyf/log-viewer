package com.lsyf.pay.logger.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lsyf.pay.logger.common.dto.Segment;
import com.lsyf.pay.logger.common.res.AppLog;
import com.lsyf.pay.logger.common.res.Log;
import com.lsyf.pay.logger.service.res.SWRes;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 数据处理
 */
public class DataUtil {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static List<AppLog> processESRes(List<String> strings) {
        List<String> unparsed = Lists.newArrayList();
        //先解析日志，并按时间排序
        List<Log> logs = strings.stream()
                .filter(s -> StringUtils.hasText(s))
                .map(s -> {
                    Log log = parse(s);
                    //解析失败
                    if (log == null) {
                        unparsed.add(s);
                    }
                    return log;
                })
                .filter(l -> l != null)
                .collect(Collectors.toList());


        //补充segment关系和日志
        List<AppLog> list = getAppLogs(logs);

        if (!CollectionUtils.isEmpty(unparsed)) {
            //加入无法解析的日志
            list.add(AppLog.builder()
                    .serviceCode("cannot parsed")
                    .logs(unparsed.stream()
                            .map(str -> Log.builder().source(str).build())
                            .collect(Collectors.toList()))
                    .lvl(0)
                    .error(true)
                    .build());
        }
        return list;
    }

    public static void main(String[] args) {
        String log = "2020-07-09 00:57:02,073 [activity] [WARN] [TID: 963.67.15942562220738065||963.67.15942562220738064] [ExceptionAdvice.java :25(handleException)] Api error\n" +
                "org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' not supported\n" +
                "\tat org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.handleNoMatch(RequestMappingInfoHandlerMapping.java:200) ~[spring-webmvc-5.1.8.RELEASE.jar!/:5.1.8.RELEASE]\n" +
                "\tat org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.lookupHandlerMethod(AbstractHandlerMethodMapping.java:419) ~[spring-webmvc-5.1.8.RELEASE.jar!/:5.1.8.RELEASE]";


        System.out.println(parse(log));
    }

    private static List<AppLog> getAppLogs(List<Log> logs) {
        Map<String, AppLog> map = Maps.newLinkedHashMap();
        Map<String, List<AppLog>> kidsMap = Maps.newLinkedHashMap();

        //遍历所有日志，按sId分组log，按pId分组appLog
        Map<String, List<Log>> logMap = logs.stream()
                .peek(l -> {
                    String sId = l.getSId();
                    AppLog log = map.get(sId);
                    //map中没有则新增appLog
                    if (log == null) {
                        log = AppLog.builder()
                                .segmentId(sId)
                                .traceId(l.getTraceId())
                                .pSegmentId(l.getPId())
                                .startTime(l.getTime())
                                .endTime(l.getTime())
                                .error(l.isError())
                                .serviceCode(l.getServiceCode())
                                .build();
                        map.put(sId, log);

                        //按父id分组
                        String pId = l.getPId();
                        List<AppLog> kids = kidsMap.getOrDefault(pId, Lists.newArrayList());
                        kids.add(log);
                        kidsMap.putIfAbsent(pId, kids);
                    } else {
                        //map中有则更新结束时间和异常状态
                        if (l.getTime().compareTo(log.getEndTime()) > 0) {
                            log.setEndTime(l.getTime());
                        }
                        log.setError(log.isError() || l.isError());
                    }

                    //清空无需字段，降低网络开销
                    l.clear();
                })
                .collect(Collectors.groupingBy(Log::getSId));


        //补充信息
        List<AppLog> list0 = Lists.newArrayList();
        map.forEach((sId, log) -> {
            AppLog pLog = map.get(log.getPSegmentId());
            log.setPServiceCode(pLog != null ? pLog.getServiceCode() : null);

            //找不到父segment则级别默认为0
            if (pLog == null) {
                log.setLvl(0);
                list0.add(log);
            }
            //如果父segment的serviceCode和子segment一致则默认为创建新线程
            if (pLog != null && log.getServiceCode().equals(pLog.getServiceCode())) {
                log.setNewThread(true);
            }
            try {
                //计算耗时
                log.setCostTime(
                        Duration.between(LocalDateTime.parse(log.getStartTime(), dtf),
                                LocalDateTime.parse(log.getEndTime(), dtf)).toMillis());
            } catch (Exception e) {
            }
            log.setLogs(logMap.get(sId));
        });

        //如果没有0级别
        if (list0.isEmpty()) {
            list0.addAll(map.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .filter(l -> {
                        //循环的均放入0级别
                        AppLog p = map.get(l.getPSegmentId());
                        return p != null && l.getSegmentId().equals(p.getPSegmentId());
                    })
                    .peek(l -> l.setLvl(0))
                    .collect(Collectors.toList()));
        }

        //排序及级别计算
        //深度优先遍历
        Stack<AppLog> stack = new Stack<>();
        stack.addAll(list0.stream().sorted(Comparator.comparing(AppLog::getStartTime).reversed()).collect(Collectors.toList()));
        List<AppLog> list = Lists.newArrayList();
        while (!stack.isEmpty()) {
            AppLog log = stack.pop();
            list.add(log);
            stack.addAll(kidsMap.getOrDefault(log.getSegmentId(), Lists.newArrayList())
                    .stream()
                    .filter(l -> l.getLvl() == null)
                    .sorted(Comparator.comparing(AppLog::getStartTime).reversed())
                    .peek(l -> l.setLvl(log.getLvl() + 1))
                    .collect(Collectors.toList()));
        }

        return list;
    }


    public static Log parse(String s) {
        s = s.trim();
        String source = s;
        //日志前19位是时间
        String time = s.substring(0, 23).trim();

        //第一个[]中存放服务名
        int a = s.indexOf("]");
        String serviceCode = s.substring(s.indexOf("[") + 1, a).trim();

        //根据是否存在[ERROR]判断是否存在异常
        a++;
        String level = s.substring(s.indexOf("[", a) + 1, s.indexOf("]", a)).trim().toLowerCase();
        boolean isError = "error".equalsIgnoreCase(level);

        //根据[TID: 获取 skywalking相关id
        int i = s.indexOf("[TID:");
        String swId = s.substring(i + 5, s.indexOf("]", i)).trim();
        String[] temp = swId.split("\\|");
        if (temp.length != 3) {
            return null;
        }
        String traceId = temp[0];
        String pId = temp[1];
        String sId = temp[2];

        return Log.builder()
                .serviceCode(serviceCode)
                .traceId(traceId)
                .sId(sId)
                .pId(pId)
                .time(time)
                .error(isError)
                .level(level)
                .source(source)
                .build();
    }


    public static List<Segment> processSWRes(SWRes swRes) {
        List<SWRes.Span> spans = swRes.getSpans();
        //根据segmentId分组
        Map<String, List<SWRes.Span>> map = spans.stream().collect(Collectors.groupingBy(SWRes.Span::getSegmentId));
        List<Segment> list = Lists.newArrayList();

        //归并segment信息
        map.forEach((id, l) -> {
            Segment s = Segment.builder()
                    .segmentId(id)
                    .startTime(Long.MAX_VALUE)
                    .endTime(Long.MIN_VALUE)
                    .error(false)
                    .build();
            for (SWRes.Span span : l) {
                //开始时间取最小值
                if (span.getStartTime() < s.getStartTime()) {
                    s.setStartTime(span.getStartTime());
                }
                //结束时间取最大值
                if (span.getEndTime() > s.getEndTime()) {
                    s.setEndTime(span.getEndTime());
                }
                s.setTraceId(span.getTraceId());
                //存在一个异常span则代表异常（和根据日志判断逻辑不一致）
                s.setError(s.isError() || span.isError());
                if (!CollectionUtils.isEmpty(span.getRefs())) {
                    SWRes.SpanRef ref = span.getRefs().get(0);
                    s.setPSegmentId(ref.getParentSegmentId());
                    List<SWRes.Span> temp = map.get(ref.getParentSegmentId());
                    s.setPServiceCode(temp != null ? temp.get(0).getServiceCode() : null);
                    s.setRefType(ref.getType());
                }
                s.setServiceCode(span.getServiceCode());
            }
            list.add(s);
        });

        return list.stream()
                .sorted(Comparator.comparingLong(Segment::getStartTime))
                .collect(Collectors.toList());
    }

}
