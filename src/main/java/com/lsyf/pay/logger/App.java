package com.lsyf.pay.logger;

import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author liufei
 * @Date 2019/9/18
 */
@SpringBootApplication
@EnableFeignClients
@EnableSwagger2Doc
@Slf4j
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
        log.info("Application run succeed");
    }
}
