package com.ccsu.community.dto;

import lombok.Data;

@Data
public class MyUserDTO {
    private String accountId;
    private String password;
    private String token;
    private Integer verityCode;
}
