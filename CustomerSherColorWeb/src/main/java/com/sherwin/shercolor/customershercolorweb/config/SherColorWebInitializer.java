package com.sherwin.shercolor.customershercolorweb.config;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

// For now, this will set the 'oracle' profile, connecting us to the Oracle datasource
// This can also be set through a command line argument like so '-Dspring.profiles.active=postgres'
public class SherColorWebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.profiles.include","oracle");
    }
}
