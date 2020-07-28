package com.sher.simpleblog.service.impl;

import com.sher.simpleblog.entity.Blog;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.repository.BlogRepository;
import com.sher.simpleblog.service.BlogService;
import com.sher.simpleblog.util.MyBeanUtils;
import com.sher.simpleblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;

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
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery("%" + query + "%", pageable);
    }

    @Override
    public List<Blog> listBlogTopRecommend(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTopRecommend(pageable);
    }

    @Override
    public Page<Blog> listBlogByTypeId(Long typeId, Pageable pageable) {
        return blogRepository.findByTypeId(typeId, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupByYear();
        Map<String, List<Blog>> map = new TreeMap<>((a, b) -> {
            return b.compareTo(a);
        });
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public int updateView(Long id) {
        return blogRepository.updateView(id);
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
        blog.setUpdateTime(new Date());
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new NotFoundException();
        }
        Blog b = optionalBlog.get();
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyName(blog));

        Blog save = blogRepository.save(b);
        return save;
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

}
