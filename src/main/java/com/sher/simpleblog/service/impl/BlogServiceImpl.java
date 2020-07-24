package com.sher.simpleblog.service.impl;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.repository.BlogRepository;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author sher
 */
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public Blog getBlog(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new NotFoundException();
        }
        return optionalBlog.get();
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(
                (Specification<Blog>) (root, criteriaQuery, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!StringUtils.isEmpty(blog.getTitle())) {
                        predicates.add(criteriaBuilder.like(
                                root.get("title"), "%" + blog.getTitle() + "%")
                        );
                    }

                    if (blog.getTypeId() != null) {
                        predicates.add(criteriaBuilder.equal(
                                root.get("type").get("id"), blog.getTypeId())
                        );
                    }

                    if (blog.isRecommend()) {
                        predicates.add(criteriaBuilder.equal(
                                root.get("recommend"), blog.isRecommend()
                        ));
                    }

                    criteriaQuery.where(predicates.toArray(new Predicate[0]));
                    return null;
                },
                pageable);
    }

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        return blogRepository.save(blog);
    }

    @Override
    @Transactional
    public Blog updateBlog(Long id, Blog blog) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new NotFoundException();
        }
        return optionalBlog.get();
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

}
