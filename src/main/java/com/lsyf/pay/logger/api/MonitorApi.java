package com.lsyf.pay.logger.api;

import com.lsyf.pay.logger.common.ApiResponse;
import com.lsyf.pay.logger.service.MonitorClient;
import com.lsyf.pay.logger.service.res.Monitor1Res;
import com.lsyf.pay.logger.service.res.Monitor2Res;
import com.lsyf.pay.logger.service.res.UserIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author liufei
 * @Date 2019/11/20
 */
@Controller
@RequestMapping("monitor")
public class MonitorApi {

    @Autowired
    MonitorClient client;


    @GetMapping
    public String index() {
        return "monitor/all";
    }


    @GetMapping("/1")
    @ResponseBody
    public ApiResponse<List<Monitor1Res>> monitor1(String date,
                                                   int num,
                                                   UserIdType type) {
        return ApiResponse.success(client.monitor1(date, num, type));
    }

    @GetMapping("/2")
    @ResponseBody
    public ApiResponse<List<Monitor2Res>> monitor2(String date,
                                                   int num,
                                                   UserIdType type) {
        return ApiResponse.success(client.monitor2(date, num, type));
    }


}
