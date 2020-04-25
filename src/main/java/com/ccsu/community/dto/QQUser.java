package com.ccsu.community.dto;

import lombok.Data;

/**
 * @author 华华
 */
@Data
public class QQUser {
    /**
     * 返回码
     */
    private Long ret;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * qq空间名称
     */
    private String nickname;
    /**
     * 性别
     */
    private String gender;
    /**
     * //40*40头像
     */
    private String figureurl_qq_1;
}
