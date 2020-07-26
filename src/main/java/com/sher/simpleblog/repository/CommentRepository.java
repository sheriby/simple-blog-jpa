package com.sher.simpleblog.repository;

import com.sher.simpleblog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Title CommentRepository
 * @Package com.sher.simpleblog.repository
 * @Description jpa repository for entity comment
 * @Author sher
 * @Date 2020/07/26 4:21 PM
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.blog.id = ?1 and c.parentComment is null")
    List<Comment> findByBlogIdNoParent(Long id, Sort sort);
}
