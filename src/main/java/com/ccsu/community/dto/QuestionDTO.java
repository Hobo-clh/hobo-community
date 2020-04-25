package com.ccsu.community.dto;

import com.ccsu.community.model.User;
import lombok.Data;

/**
 * @author 华华
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private User user;
    private Integer top;
}
