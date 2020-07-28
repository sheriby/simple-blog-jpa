package com.sher.simpleblog.controller;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Title ArchiveController
 * @Package com.sher.simpleblog.controller
 * @Description handle request /archive
 * @Author sher
 * @Date 2020/07/27 2:12 PM
 */
@Controller
@Slf4j
@RequestMapping("/archive")
public class ArchiveController {

    private final BlogService blogService;

    public ArchiveController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public String archive(Model model) {
        Map<String, List<Blog>> blogMap = blogService.archiveBlog();

        int blogNums = 0;
        for (List<Blog> blogs : blogMap.values()) {
            blogNums += blogs.size();
        }

        model.addAttribute("blogMap", blogMap);
        model.addAttribute("blogNums", blogNums);

        return "archive";
    }
}
