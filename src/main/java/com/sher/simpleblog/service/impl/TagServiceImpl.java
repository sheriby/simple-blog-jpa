package com.sher.simpleblog.service.impl;

import com.sher.simpleblog.entity.Tag;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.repository.TagRepository;
import com.sher.simpleblog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Tag saveTag(Tag type) {
        return tagRepository.save(type);
    }

    @Override
    @Transactional
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToIds(ids));
    }

    private List<Long> convertToIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<Long>();
        }
        List<Long> idList = new ArrayList<>();
        String[] idArray = ids.split(",");
        for (String s : idArray) {
            try {
                idList.add(Long.valueOf(s));
            } catch (Exception e) {
                Tag tag = new Tag();
                tag.setName(s);
                Tag save = tagRepository.save(tag);
                idList.add(save.getId());
            }
        }
        return idList;
    }

    @Override
    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> res = tagRepository.findById(id);
        if (res.isEmpty()) {
            throw new NotFoundException();
        }
        Tag t = res.get();
        BeanUtils.copyProperties(tag, t);

        return tagRepository.save(t);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }
}
