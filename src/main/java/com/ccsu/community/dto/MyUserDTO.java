package com.ccsu.community.dto;

import lombok.Data;

@Data
public class MyUserDTO {
    private String loginName;
    private String password;
    private String token;
}
