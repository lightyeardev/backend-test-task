package com.golightyear.backend.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JooqConfig {

    @PostConstruct
    void init() {
        System.getProperties().setProperty("org.jooq.no-logo", "true");
    }

}
