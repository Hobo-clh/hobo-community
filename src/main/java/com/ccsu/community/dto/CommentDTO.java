package com.ccsu.community.dto;

import com.ccsu.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private String content;
    private Integer type;
    private Long commentator;
    private Long likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentCount;
    private User user;

}
