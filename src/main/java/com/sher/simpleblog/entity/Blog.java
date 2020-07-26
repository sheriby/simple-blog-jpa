package com.sher.simpleblog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"type", "tags", "user", "comments", "content", "description"})
@Entity
@Table(name = "tb_blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private String picture;
    private String flag;
    private Integer views;
    @Column(columnDefinition = "varchar(512)")
    private String description;

    private boolean appreciation;
    private boolean shareStatement;
    private boolean comment;
    private boolean publish;
    private boolean recommend;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    private Type type;

    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @Transient
    private String tagIds;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();


    public Blog initTagIds() {
        if (!StringUtils.isEmpty(tagIds)) {
            return this;
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        for (Tag tag: tags) {
            if (flag) {
                sb.append(",");
            } else {
                flag = true;
            }
            sb.append(tag.getId());
        }
        tagIds = sb.toString();
        return this;
    }
}
