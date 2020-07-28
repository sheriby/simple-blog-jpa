package com.sher.simpleblog.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Title MyPageUtils
 * @Package com.sher.simpleblog.util
 * @Description convert list to page
 * @Author sher
 * @Date 2020/07/27 1:49 PM
 */
public class MyPageUtils {

    public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
}
