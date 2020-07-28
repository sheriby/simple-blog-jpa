package com.sher.simpleblog.service;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author sher
 */
public interface BlogService {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, String query);

    List<Blog> listBlogTopRecommend(Integer size);

    Page<Blog> listBlogByTypeId(Long typeId, Pageable pageable);

    Map<String, List<Blog>> archiveBlog();

    int updateView(Long id);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
