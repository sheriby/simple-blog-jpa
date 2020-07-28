package com.sher.simpleblog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"blog", "parentComment"})
@NoArgsConstructor
@Entity
@Table(name = "tb_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String nickname;
    private String email;
    private String content;
    private String avatar;
    private String replyNickname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private Blog blog;

    @ManyToOne
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();
}
