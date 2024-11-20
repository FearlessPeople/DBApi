package com.dbapi;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.dbapi.dao")
public class DBApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(DBApiApplication.class);

    /**
     * 设置时区
     */
    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DBApiApplication.class);

        // 添加一个监听器，在应用启动时打印日志信息
        app.addListeners((ApplicationListener<ApplicationStartedEvent>) event -> {
            Environment environment = event.getApplicationContext().getEnvironment();
            printStartupInfo(environment);
        });

        // 启动应用
        app.run(args);
    }

    /**
     * 打印启动信息
     */
    private static void printStartupInfo(Environment environment) {
        String version = "1.0.0";
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String port = environment.getProperty("server.port", "8088");
        String url = "http://localhost:" + port + "/";

        logger.info("==========================================");
        logger.info("欢迎使用 DB API " + version + "应用");
        logger.info("启动时间: " + startTime);
        logger.info("当前时区: " + TimeZone.getDefault().getID());
        logger.info("访问链接: " + url);
        logger.info("==========================================");
    }
}
