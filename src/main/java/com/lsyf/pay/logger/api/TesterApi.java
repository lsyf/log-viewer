package com.lsyf.pay.logger.api;

import com.lsyf.pay.logger.common.dto.Env;
import com.lsyf.pay.logger.common.req.DubboGenericReq;
import com.lsyf.pay.logger.service.TesterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author liufei
 * @Date 2019/11/3
 */
@RestController
@RequestMapping("tester")
@Api(value = "tester", tags = "tester")
@Validated
public class TesterApi {

    @Autowired
    TesterService testerService;

    @PostMapping(value = "/invoke/{env}/{interfacE}/{method}")
    @ApiOperation("动态调用")
    public Object invoke(
            @ApiParam(value = "环境", defaultValue = "test", required = true) @PathVariable("env") @NotNull Env env,
            @ApiParam(value = "接口", required = true) @PathVariable("interfacE") @NotBlank String interfacE,
            @ApiParam(value = "方法", required = true) @PathVariable("method") @NotBlank String method,
            @NotNull DubboGenericReq req) {
        return testerService.invoke(env,
                notnull(interfacE),
                notnull(req.getGroup()),
                notnull(req.getVersion()),
                notnull(method),
                req);
    }

    private String notnull(String str) {
        return str == null ? "" : str.trim();
    }

}
