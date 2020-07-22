package com.sher.simpleblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/category").setViewName("category");
        registry.addViewController("/label").setViewName("label");
        registry.addViewController("/archive").setViewName("archive");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/login").setViewName("admin/login");
    }
}
