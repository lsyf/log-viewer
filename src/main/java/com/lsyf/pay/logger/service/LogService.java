package com.lsyf.pay.logger.service;

import com.lsyf.pay.logger.common.res.AppLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @Author liufei
 * @Date 2019/7/1
 */
@Service
@Slf4j
public class LogService {


    @Autowired
    ESClient client;


    public List<AppLog> es(String traceId) {
        Assert.isTrue(StringUtils.isNotBlank(traceId), "traceId不能为空");
        //先查询数据
        List<String> logs = client.getLogs(traceId);

        return DataUtil.processESRes(logs);
    }

}
