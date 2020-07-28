package com.sher.simpleblog.interceptor;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.service.BlogService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Title TopBlogInterceptor
 * @Package com.sher.simpleblog.interceptor
 * @Description add top blog information to session
 * @Author sher
 * @Date 2020/07/27 2:15 PM
 */
@Component
public class TopBlogInterceptor extends HandlerInterceptorAdapter {

    private final BlogService blogService;

    public TopBlogInterceptor(BlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("topBlogs") == null) {
            List<Blog> blogs = blogService.listBlogTopRecommend(4);
            session.setAttribute("topBlogs", blogs);
        }

        return true;
    }
}
