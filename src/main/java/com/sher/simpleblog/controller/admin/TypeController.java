package com.sher.simpleblog.controller.admin;

import com.sher.simpleblog.entity.Type;
import com.sher.simpleblog.service.TypeService;
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
@RequestMapping("/admin/type")
@Slf4j
public class TypeController {

    private final TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public String type(@PageableDefault(size = 5, sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/type";
    }

    @RequestMapping("/input")
    public String inputType(Model model) {
        Object type = model.getAttribute("type");
        model.addAttribute("type", new Type());
        return "admin/type_input";
    }

    @PostMapping
    public String saveType(Type type, Model model) {
        String url = "forward:/admin/type/input";
        if (type.getId() != null) {
            url += "/" + type.getId();
        }
        if (StringUtils.isEmpty(type.getName())) {
            model.addAttribute("message", "分类名不能为空");
            return url;
        }

        Type t = typeService.getTypeByName(type.getName());
        if (t != null) {
            model.addAttribute("message", "添加失败，该分类已经存在");
            return url;
        }

        if (type.getId() != null) {
            t = typeService.updateType(type.getId(), type);
        } else {
            t = typeService.saveType(type);
        }

        if (t == null) {
            model.addAttribute("message", "添加失败，请检查分类名");
            return url;
        } else {
            return "redirect:/admin/type";
        }

    }

    @RequestMapping("/input/{id}")
    public String editType(@PathVariable Long id, Model model) {
        Type type = typeService.getType(id);
        model.addAttribute("type", type);

        return "/admin/type_input";
    }

    @GetMapping("/delete/{id}")
    public String deleteType(@PathVariable Long id, Model model) {
        typeService.deleteType(id);
        return "forward:/admin/type";
    }

}
