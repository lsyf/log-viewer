package com.lsyf.pay.logger.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lsyf.pay.logger.common.util.StringUtil;
import com.lsyf.pay.logger.service.properties.ESProperties;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author liufei
 * @Date 2019/7/1
 */
@Service
@Slf4j
public class ESClient {

    @Autowired
    JestClient logClient;

    @Autowired
    ESProperties properties;


    public List<String> getLogs(String traceId) {
        List<String> logs = Lists.newArrayList();
        //如果索引是动态的
        if (Boolean.TRUE.equals(properties.getDynamicIndex())) {
            //首先根据tid计算时间
            LocalDate ld = getDate(traceId);

            //查询前后两天的索引
            for (int i = -1; i <= 1; i++) {
                LocalDate x = ld.plusDays(i);
                Map<String, Object> map = Maps.newHashMap();
                map.put("yyyy", x.getYear());
                map.put("MM", StringUtil.frontWithZore(x.getMonthValue(), 2));
                map.put("dd", StringUtil.frontWithZore(x.getDayOfMonth(), 2));
                String index = StringUtil.replace(properties.getIndex(), map, "x{", "}", false);
                logs.addAll(getLogs(traceId, index));

            }
        } else {
            String indexes = properties.getIndex();
            if (indexes.indexOf(",") != -1) {
                String[] indexArr = indexes.split(",");
                for (String idx : indexArr) {
                    logs.addAll(getLogs(traceId, idx));
                }
            } else {
                logs = getLogs(traceId, indexes);
            }
        }

        return logs;
    }


    private LocalDate getDate(String traceId) {
        if (!StringUtils.hasText(traceId)) {
            return LocalDate.now();
        }
        try {
            String[] strs = traceId.split("\\.");
            long timestamp = Long.parseLong(strs[2]) / 10000;
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                    .toLocalDate();
        } catch (Exception e) {
            log.warn("解析traceId转换时间失败：traceId={}", traceId);
            return LocalDate.now();
        }
    }

    private List<String> getLogs(String traceId, String index) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("sort", "asc");
        map.put("size", properties.getSize());
        map.put("traceId", traceId);
        String query = StringUtil.replace(properties.getQuery(), map, "x{", "}", false);

        Search search = new Search.Builder(query)
                .addIndex(index)
                .build();

        try {
            SearchResult result = logClient.execute(search);
            List<String> logs = result.getSourceAsStringList();
            return Optional.ofNullable(logs).orElse(Lists.newArrayList())
                    .stream()
                    .map(s -> JSON.parseObject(s).getString("message"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("查询es异常", e);
        }
        return Lists.newArrayList();
    }


}
