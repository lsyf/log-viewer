package com.lsyf.pay.logger.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "monitor",
        ignoreUnknownFields = false)
@Data
public class MonitorProperties {
    String query1;
    String query2;

}
