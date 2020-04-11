package com.ccsu.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String tag;
    private String search;
    private Integer page;
    private Integer size;
    private Long time;
    private String sort;
}
