package com.lsyf.pay.logger.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lsyf.pay.logger.common.util.StringUtil;
import com.lsyf.pay.logger.common.util.TimeUtil;
import com.lsyf.pay.logger.service.properties.MonitorProperties;
import com.lsyf.pay.logger.service.res.Monitor1Res;
import com.lsyf.pay.logger.service.res.Monitor2Res;
import com.lsyf.pay.logger.service.res.UserIdType;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @Author liufei
 * @Date 2019/7/1
 */
@Service
@Slf4j
public class MonitorClient {

    @Autowired
    JestClient logClient;

    @Autowired
    MonitorProperties properties;
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");


    public List<Monitor1Res> monitor1(String date, int num, UserIdType userIdType) {
        date = date + "01";
        LocalDate ld = LocalDate.parse(date, yyyyMMdd);
        Map<String, Object> map = Maps.newHashMap();
        map.put("from", TimeUtil.toDate(ld).getTime());
        map.put("to", TimeUtil.toDate(ld.plusMonths(1)).getTime());
        map.put("num", num);
        switch (userIdType) {
            case cpf:
                map.put("userField", "k_id_no.keyword");
                break;
            case bankNO:
                map.put("userField", "ext_info_json.cardNo.keyword");
                break;
            default:
                throw new RuntimeException("不支持userIdType");
        }
        String query = StringUtil.replace(properties.getQuery1(), map, "x{", "}", false);


        Search search = new Search.Builder(query)
                .addIndex("db_order")
                .addType("db_order")
                .build();
        try {
            SearchResult result = logClient.execute(search);
            return Monitor1Res.parse(result, userIdType);
        } catch (IOException e) {
            log.warn("查询es异常", e);
        }
        return Lists.newArrayList();
    }

    public List<Monitor2Res> monitor2(String date, double amount, UserIdType userIdType) {
        LocalDate ld = LocalDate.parse(date, yyyyMMdd);
        Map<String, Object> map = Maps.newHashMap();
        map.put("from", TimeUtil.toDate(ld).getTime());
        map.put("to", TimeUtil.toDate(ld.plusDays(1)).getTime());
        map.put("amount", toLong(amount));
        switch (userIdType) {
            case cpf:
                map.put("userField", "k_id_no.keyword");
                break;
            case bankNO:
                map.put("userField", "ext_info_json.cardNo.keyword");
                break;
            default:
                throw new RuntimeException("不支持userIdType");
        }
        String query = StringUtil.replace(properties.getQuery2(), map, "x{", "}", false);


        Search search = new Search.Builder(query)
                .addIndex("db_order")
                .addType("db_order")
                .build();
        try {
            SearchResult result = logClient.execute(search);
            return Monitor2Res.parse(result, userIdType);
        } catch (IOException e) {
            log.warn("查询es异常", e);
        }
        return Lists.newArrayList();
    }

    public long toLong(double d) {
        return new Double(d * 1000).longValue();
    }

    public double toDouble(long l) {
        return new Double(l) / 1000;
    }

}
