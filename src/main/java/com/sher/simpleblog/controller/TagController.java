package com.sher.simpleblog.controller;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.entity.Tag;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.service.TagService;
import com.sher.simpleblog.util.MyPageUtils;
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
 * @Title TagController
 * @Package com.sher.simpleblog.controller
 * @Description handler the request about tag
 * @Author sher
 * @Date 2020/07/27 1:28 PM
 */
@Controller("indexTagController")
@Slf4j
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping
    public String tag(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                              Pageable pageable, Model model) {
        List<Tag> tags = tagService.listTag();
        Tag tag = tags.get(0);

        return "forward:/tag/" + tag.getId();
    }

    @GetMapping("/{tagId}")
    public String tagId(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                                Pageable pageable,
                        @PathVariable Long tagId, Model model) {
        List<Tag> tags = tagService.listTag();
        model.addAttribute("tags", tags);
        if (tagService.getTag(tagId) == null) {
            throw new NotFoundException();
        }

        Tag tag = tagService.getTag(tagId);
        Page<Blog> blogs = MyPageUtils.listConvertToPage(tag.getBlogs(), pageable);
        model.addAttribute("page", blogs);
        model.addAttribute("curTagId", tagId);

        return "tag";
    }

    @PostMapping
    public String postBlogList(@PageableDefault(size = 4, sort = "updateTime", direction = Sort.Direction.DESC)
                                       Pageable pageable,
                               @RequestParam Long tagId, Model model) {
        Tag tag = tagService.getTag(tagId);
        Page<Blog> page = MyPageUtils.listConvertToPage(tag.getBlogs(), pageable);
        model.addAttribute("page", page);
        model.addAttribute("curTagId", tagId);

        return "tag :: blog_list";
    }
}
