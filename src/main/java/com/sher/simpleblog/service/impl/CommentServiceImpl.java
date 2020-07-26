package com.sher.simpleblog.service.impl;

import com.sher.simpleblog.entity.Comment;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.repository.CommentRepository;
import com.sher.simpleblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Title CommentServiceImpl
 * @Package com.sher.simpleblog.service.impl
 * @Description service implement for entity comment
 * @Author sher
 * @Date 2020/07/26 4:23 PM
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> listCommentByBlogIdNoParent(Long id) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return commentRepository.findByBlogIdNoParent(id, sort);
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            Optional<Comment> optionalComment = commentRepository.findById(parentCommentId);
            if (optionalComment.isEmpty()) {
                throw new NotFoundException();
            }

            comment.setParentComment(optionalComment.get());
        } else {
            comment.setParentComment(null);
        }

        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
