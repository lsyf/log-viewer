package com.lsyf.pay.logger.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "es6")
@Data
public class ESProperties {

    String path;
    int size;
    String index;
    String type;
    String query;
    Boolean dynamicIndex;
}
