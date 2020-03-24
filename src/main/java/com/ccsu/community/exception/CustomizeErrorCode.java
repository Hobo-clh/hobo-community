package com.ccsu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题不存在，换个试试吧~？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或者评论进行回复"),
    NOT_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器正在努力修复中 小哥哥等下再来欧~"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在了噢~");

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code,String message) {
        this.message = message;
        this.code = code;
    }


}
