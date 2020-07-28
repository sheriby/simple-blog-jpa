package com.sher.simpleblog.controller;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.entity.Type;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.service.TypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title TypeController
 * @Package com.sher.simpleblog.controller
 * @Description handler request about type
 * @Author sher
 * @Date 2020/07/27 11:26 AM
 */
@Controller("indexTypeController")
@Slf4j
@RequestMapping("/type")
public class TypeController {

    private final TypeService typeService;
    private final BlogService blogService;

    public TypeController(TypeService typeService, BlogService blogService) {
        this.typeService = typeService;
        this.blogService = blogService;
    }

    @GetMapping
    public String type(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model) {
        List<Type> types = typeService.listType();
        Long typeId = types.get(0).getId();

        return "forward:/type/"+typeId;
    }

    @GetMapping("/{typeId}")
    public String typeId(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                         Pageable pageable, @PathVariable Long typeId, Model model) {
        List<Type> types = typeService.listType();
        model.addAttribute("types", types);
        if (typeService.getType(typeId) == null) {
            throw new NotFoundException();
        }

        Page<Blog> blogs = blogService.listBlogByTypeId(typeId, pageable);
        model.addAttribute("page", blogs);
        model.addAttribute("curTypeId", typeId);

        return "type";
    }

    @PostMapping
    public String postBlogList(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                               Pageable pageable,
                               @RequestParam Long typeId, Model model) {
        Page<Blog> blogs = blogService.listBlogByTypeId(typeId, pageable);
        model.addAttribute("page", blogs);
        model.addAttribute("curTypeId", typeId);

        return "type :: blog_list";
    }
}
