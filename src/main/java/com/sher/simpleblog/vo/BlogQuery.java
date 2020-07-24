package com.sher.simpleblog.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Title BlogQuery
 * @Package com.sher.simpleblog.vo
 * @Description vo
 * @Author sher
 * @Date 2020/07/24 4:50 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;
}
