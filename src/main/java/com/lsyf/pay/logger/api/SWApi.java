package com.lsyf.pay.logger.api;

import com.google.common.collect.Maps;
import com.lsyf.pay.logger.common.ApiResponse;
import com.lsyf.pay.logger.service.SWClient;
import com.lsyf.pay.logger.service.res.SWRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

import static com.lsyf.pay.logger.common.util.StringUtil.replace;

/**
 * @Author liufei
 * @Date 2019/11/20
 */
@RestController
@RequestMapping("sw")
public class SWApi {

    @Autowired
    @Lazy
    SWClient SWClient;

    @Value("${sw.query}")
    String swQuery;
    @Value("${sw.path}")
    String swPath;

    @GetMapping("/{id}")
    public ApiResponse<SWRes> sw(@PathVariable("id") String id) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        map.put("traceId", id);
        String req = replace(swQuery, map, "x{", "}", false);
        return ApiResponse.success(SWClient.sw(new URI(swPath), req));
    }


}
