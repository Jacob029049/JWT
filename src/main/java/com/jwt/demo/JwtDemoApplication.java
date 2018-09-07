package com.jwt.demo;

import com.jwt.demo.filter.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JwtDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtDemoApplication.class, args);
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        JwtFilter jwtFilter = new JwtFilter();
        registrationBean.setFilter(jwtFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/home/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
