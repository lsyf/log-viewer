package com.lsyf.pay.logger.api;

import com.lsyf.pay.logger.common.ApiResponse;
import com.lsyf.pay.logger.common.res.AppLog;
import com.lsyf.pay.logger.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author liufei
 * @Date 2019/11/20
 */
@Controller
@RequestMapping("log")
public class LogApi {

    @Autowired
    LogService service;


    @GetMapping("/{id}")
    public String es(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("data", service.es(id));
        return "index";
    }


    @GetMapping("/{id}:json")
    @ResponseBody
    public ApiResponse<AppLog> es(@PathVariable("id") String id) {
        return ApiResponse.success(service.es(id));
    }


}
