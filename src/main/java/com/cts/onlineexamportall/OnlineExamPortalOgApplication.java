package com.cts.onlineexamportall;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineExamPortalOgApplication {

    private static final Logger logger = LogManager.getLogger(OnlineExamPortalOgApplication.class);

    public static void main(String[] args) {
        logger.info("Starting OnlineExamPortalOgApplication");
        SpringApplication.run(OnlineExamPortalOgApplication.class, args);
        logger.info("OnlineExamPortalOgApplication started successfully");
    }
}