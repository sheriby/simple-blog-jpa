package com.sher.simpleblog.controller.admin;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.entity.User;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.service.TagService;
import com.sher.simpleblog.service.TypeService;
import com.sher.simpleblog.vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * @author sher
 */
@Controller
@RequestMapping("/admin/blog")
@Slf4j
public class BlogController {

    private final BlogService blogService;
    private final TypeService typeService;
    private final TagService tagService;

    @Autowired
    public BlogController(BlogService blogService, TypeService typeService, TagService tagService) {
        this.blogService = blogService;
        this.typeService = typeService;
        this.tagService = tagService;
    }

    @GetMapping
    public String blog(@PageableDefault(size = 2, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        return "admin/blog";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 2, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blog :: blogList";
    }

    @GetMapping("/input")
    public String inputBlog(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

        return "admin/blog_input";
    }

    @PostMapping
    public String processInput(Blog blog, HttpSession session, RedirectAttributes attributes) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }


        return "redirect:/admin/blog";
    }
}
