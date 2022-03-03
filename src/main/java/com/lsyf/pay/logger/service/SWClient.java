package com.lsyf.pay.logger.service;

import com.lsyf.pay.logger.service.res.SWRes;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

/**
 * @Author liufei
 * @Date 2019/9/9
 */

@FeignClient(name = "data")
public interface SWClient {

    @ApiOperation("sw查询")
    @PostMapping
    SWRes sw(URI uri, String body);


}
