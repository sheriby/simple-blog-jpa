package com.sher.simpleblog.interceptor;

import com.sher.simpleblog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title BlogViewInterceptor
 * @Package com.sher.simpleblog.interceptor
 * @Description add blog view
 * @Author sher
 * @Date 2020/07/27 3:13 PM
 */
@Component
public class BlogViewInterceptor extends HandlerInterceptorAdapter {

    private final BlogService blogService;

    @Autowired
    public BlogViewInterceptor(BlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] split = request.getRequestURL().toString().split("/");
        String s = split[split.length - 1];
        try {
            long blogid = Long.parseLong(s);
            blogService.updateView(blogid);
        } catch (Exception e) {
            return true;
        }
        return true;
    }
}
