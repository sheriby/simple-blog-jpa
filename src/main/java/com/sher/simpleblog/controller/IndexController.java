package com.sher.simpleblog.controller;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.service.CommentService;
import com.sher.simpleblog.service.TagService;
import com.sher.simpleblog.service.TypeService;
import com.sher.simpleblog.util.MarkDownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Title IndexController
 * @Package com.sher.simpleblog.controller
 * @Description controller for index apge
 * @Author sher
 * @Date 2020/07/25 5:20 PM
 */
@Controller
@Slf4j
@SessionAttributes("topBlogs")
public class IndexController {

    private final BlogService blogService;
    private final TypeService typeService;
    private final TagService tagService;
    private final CommentService commentService;

    @Autowired
    public IndexController(BlogService blogService, TypeService typeService,
                           TagService tagService, CommentService commentService) {
        this.blogService = blogService;
        this.typeService = typeService;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    @GetMapping
    public String mainpage() {
        return "forward:/index";
    }

    @ModelAttribute("topBlogs")
    public List<Blog> topBlogs() {
        return blogService.listBlogTopRecommend(4);
    }

    @GetMapping("/index")
    public String index(@PageableDefault(size = 4, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(5));
        model.addAttribute("tags", tagService.listTagTop(10));
        return "index";
    }

    @PostMapping("/index")
    public String blogList(@PageableDefault(size = 4, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        return "index :: blog_list";
    }

    @GetMapping("/search")
    public String search(@PageableDefault(size = 4, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, query));
        model.addAttribute("query", query);
        return "search";
    }

    @PostMapping("/search")
    public String searchList(@PageableDefault(size = 4, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, query));
        return "search :: blog_list";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlog(id);
        String content = blog.getContent();

        blog.setContent(MarkDownUtils.markToHtmlExt(content));

        model.addAttribute("blog", blog);
        model.addAttribute("comments", commentService.listCommentByBlogIdNoParent(id));

        return "blog";
    }
}
