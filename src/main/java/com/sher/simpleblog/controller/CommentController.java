package com.sher.simpleblog.controller;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.entity.Comment;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title CommentController
 * @Package com.sher.simpleblog.controller
 * @Description handler the request aboui blog comment
 * @Author sher
 * @Date 2020/07/26 4:15 PM
 */
@Controller
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final BlogService blogService;

    @Value("${comments.avatar}")
    private String avatar;

    @Autowired
    public CommentController(CommentService commentService, BlogService blogService) {
        this.commentService = commentService;
        this.blogService = blogService;
    }

    @GetMapping("/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogIdNoParent(blogId));
        return "blog :: comment_list";
    }

    @PostMapping
    public String processComment(Comment comment) {
        Long blogId = comment.getBlog().getId();
        Blog blog = blogService.getBlog(blogId);

        comment.setAvatar(avatar);

        commentService.save(comment);

        return "redirect:/comment/" + blogId;
    }
}
