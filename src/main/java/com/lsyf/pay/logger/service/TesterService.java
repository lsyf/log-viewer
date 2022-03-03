package com.lsyf.pay.logger.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.lsyf.pay.logger.common.dto.Env;
import com.lsyf.pay.logger.common.req.DubboGenericReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author liufei
 * @Date 2019/11/7
 */
@Service
@Slf4j
public class TesterService {

    @Autowired
    @Qualifier("devApplicationConfig")
    ApplicationConfig devApplicationConfig;

    @Autowired
    @Qualifier("testApplicationConfig")
    ApplicationConfig testApplicationConfig;


    public Object invoke(Env env,
                         String interfacE,
                         String group,
                         String version,
                         String method,
                         DubboGenericReq req) {
        log.debug("泛化调用接口：env={}，interface={}，group={}，version={}，method={}，paramInfo={}",
                env, interfacE, group, version, method, JSON.toJSONString(req));

        GenericService service = fetchService(env,
                interfacE,
                group,
                version);

        String[] parameterTypes = StringUtils.isBlank(req.getParamTypes()) ?
                new String[]{} : req.getParamTypes().trim().split(",");

        Object[] args = StringUtils.isBlank(req.getParamTypes()) ?
                new Object[]{} : JSON.parseArray(req.getParamValues()).toArray(new Object[]{});

        return service.$invoke(method,
                parameterTypes,
                args);
    }

    @Cacheable(value = "dubboCache", key = "'service_'+#env+#group+#interfacE+#version")
    public GenericService fetchService(Env env,
                                       String interfacE,
                                       String group,
                                       String version) {
        log.info("获取接口：env={}，interface={}，group={}，version={}", env, interfacE, group, version);

        ApplicationConfig applicationConfig = chooseEnv(env);

        // 配置调用信息
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setInterface(interfacE);
        reference.setGroup(group);
        reference.setVersion(version);
        reference.setGeneric(true); // 声明为泛化接口
        reference.setTimeout(60000);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        return cache.get(reference);
    }

    private ApplicationConfig chooseEnv(Env env) {
        switch (env) {
            case dev:
                return devApplicationConfig;
            case test:
                return testApplicationConfig;
            default:
                throw new RuntimeException("未知环境：" + env);
        }
    }
}
