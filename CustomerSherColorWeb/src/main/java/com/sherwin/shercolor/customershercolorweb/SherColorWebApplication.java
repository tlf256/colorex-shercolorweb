package com.sherwin.shercolor.customershercolorweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.sherwin.shercolor"})
public class SherColorWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SherColorWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SherColorWebApplication.class);
    }
}