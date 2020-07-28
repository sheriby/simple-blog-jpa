package com.sher.simpleblog.config;

import com.sher.simpleblog.interceptor.BlogViewInterceptor;
import com.sher.simpleblog.interceptor.LoginInterceptor;
import com.sher.simpleblog.interceptor.TopBlogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TopBlogInterceptor topBlogInterceptor;

    @Autowired
    private BlogViewInterceptor blogViewInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("admin/login");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
        registry.addInterceptor(topBlogInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(blogViewInterceptor)
                .addPathPatterns("/blog/*");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/", "file:static/");
//    }
}
