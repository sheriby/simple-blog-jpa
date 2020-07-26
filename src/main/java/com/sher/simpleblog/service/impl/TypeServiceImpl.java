package com.sher.simpleblog.service.impl;

import com.sher.simpleblog.entity.Type;
import com.sher.simpleblog.exception.NotFoundException;
import com.sher.simpleblog.repository.TypeRepository;
import com.sher.simpleblog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    @Autowired
    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    @Transactional
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    @Transactional
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort =Sort.by(Sort.Direction.DESC, "blogs.size");
        PageRequest pageable = PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageable);
    }

    @Override
    @Transactional
    public Type updateType(Long id, Type type) {
        Optional<Type> res = typeRepository.findById(id);
        if (res.isEmpty()) {
            throw new NotFoundException();
        }
        Type t = res.get();
        BeanUtils.copyProperties(type, t);

        return typeRepository.save(t);
    }

    @Override
    @Transactional
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }
}
