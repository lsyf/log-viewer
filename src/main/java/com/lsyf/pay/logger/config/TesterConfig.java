package com.lsyf.pay.logger.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Author liufei
 * @Date 2019/11/7
 */
@Configuration
@ConfigurationProperties(prefix = "tester")
@Slf4j
@Setter
public class TesterConfig {
    String name;
    String devAddress;
    String testAddress;

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        return objectMapper;
    }

    @Bean
    public ApplicationConfig devApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(name);
        config.setRegistry(new RegistryConfig(devAddress));
        return config;
    }

    @Bean
    public ApplicationConfig testApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(name);
        config.setRegistry(new RegistryConfig(testAddress));
        return config;
    }


}
