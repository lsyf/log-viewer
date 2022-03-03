package com.lsyf.pay.logger.config;

import com.lsyf.pay.logger.service.properties.ESProperties;
import com.lsyf.pay.logger.service.properties.MonitorProperties;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ESProperties.class, MonitorProperties.class})
public class ESConfig {
    @Autowired
    ESProperties esProperties;


    @Bean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(esProperties.getPath())
                .build());
        return factory.getObject();
    }

}
