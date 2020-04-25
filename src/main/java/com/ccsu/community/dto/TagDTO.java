package com.ccsu.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 华华
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
