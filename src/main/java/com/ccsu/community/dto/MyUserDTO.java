package com.ccsu.community.dto;

import lombok.Data;

/**
 * @author 华华
 * @date 20/05/14
 **/
@Data
public class MyUserDTO {
    /**
     * 用户名
     */
    String loginName;
    /**
     * 签名
     */
    String bio;
    /**
     * 个人头像
     */
    String avatarUrl;
}
