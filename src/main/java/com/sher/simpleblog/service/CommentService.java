package com.sher.simpleblog.service;

import com.sher.simpleblog.entity.Comment;

import java.util.List;

/**
 * @Title CommentService
 * @Package com.sher.simpleblog.service
 * @Description service interface for entity comment
 * @Author sher
 * @Date 2020/07/26 4:22 PM
 */
public interface CommentService {

    List<Comment> listCommentByBlogIdNoParent(Long id);

    Comment save(Comment comment);
}
