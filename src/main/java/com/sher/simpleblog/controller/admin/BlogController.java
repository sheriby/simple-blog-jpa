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
import org.springframework.web.bind.annotation.*;
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
    public String blog(@PageableDefault(size = 5, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        return "admin/blog";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = "updateTime",
            direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blog :: blogList";
    }

    @GetMapping("/input")
    public String inputBlog(Model model) {
        Blog blog = new Blog();
        blog.setRecommend(true);
        blog.setShareStatement(true);
        blog.setAppreciation(true);
        blog.setComment(true);
        model.addAttribute("blog", blog);
        getTypeAndTags(model);

        return "admin/blog_input";
    }

    private void getTypeAndTags(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/input/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        getTypeAndTags(model);
        model.addAttribute("blog", blogService.getBlog(id).initTagIds());

        return "admin/blog_input";
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public void deleteBlog(@PathVariable Long id) {
        System.out.println(id);
        blogService.deleteBlog(id);

//        return "redirect:/admin/blog";
    }

    @PostMapping
    public String processInput(Blog blog, HttpSession session, RedirectAttributes attributes) {
        System.out.println(blog);
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b = null;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }


        return "redirect:/admin/blog";
    }
}
