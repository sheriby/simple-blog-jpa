package com.sher.simpleblog.controller.admin;

import com.sher.simpleblog.entity.Tag;
import com.sher.simpleblog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tag")
@Slf4j
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String tag(@PageableDefault(size = 5, sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tag";
    }

    @RequestMapping("/input")
    public String inputTag(Model model) {
        Object tag = model.getAttribute("tag");
        model.addAttribute("tag", new Tag());
        return "admin/tag_input";
    }

    @PostMapping
    public String saveTag(Tag tag, Model model) {
        if (tag.getName().contains(",")){
            return saveTags(tag.getName());
        }
        String url = "forward:/admin/tag/input";
        if (tag.getId() != null) {
            url += "/" + tag.getId();
        }

        if (StringUtils.isEmpty(tag.getName())) {
            model.addAttribute("message", "分类名不能为空");
            return url;
        }

        Tag t = tagService.getTagByName(tag.getName());
        if (t != null) {
            model.addAttribute("message", "添加失败，该分类已经存在");
            return url;
        }

        if (tag.getId() != null) {
            t = tagService.updateTag(tag.getId(), tag);
        } else {
            t = tagService.saveTag(tag);
        }

        if (t == null) {
            model.addAttribute("message", "添加失败，请检查分类名");
            return url;
        } else {
            return "redirect:/admin/tag";
        }

    }

    private String saveTags(String tags) {
        String[] tagName = tags.split(",");
        for (String tag : tagName) {
            Tag tagByName = tagService.getTagByName(tag);
            if (tagByName != null) {
                continue;
            }
            Tag t = new Tag();
            t.setName(tag);
            Tag tag1 = tagService.saveTag(t);

            if (tag1 == null) {
                throw new RuntimeException();
            }
        }
        return "redirect:/admin/tag";
    }

    @RequestMapping("/input/{id}")
    public String editTag(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag", tag);

        return "/admin/tag_input";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id, Model model) {
        tagService.deleteTag(id);
        return "forward:/admin/tag";
    }

}
