package com.ccsu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题不存在，换个试试吧~？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或者评论进行回复"),
    NOT_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器正在努力修复中 小哥哥等下再来欧~"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"评论不存在了噢~"),
    CONTENT_IS_EMPTY(2007,"输入的内容不能为空~"),
    FILE_UPLOAD_FAIL(2008,"图片上传失败"),
    QUESTION_CREATOR_ERROR(2009,"这个提问不属于你噢，你不能修改" ),
    LIKE_FAILURE(2010,"你已经赞过啦!!" ),

    EMAIL_IS_EXIST(201,"邮箱已经注册过了！"),
    EMAIL_ILLEGAL(202,"邮箱不合法！"),
    EMAIL_OR_PWD_BLANK(203,"邮箱和密码都不能为空！"),
    INVALID_ADDRESSES(204,"邮箱不正确，无效的地址！"),
    VERIFY_IS_ERROR(205,"验证码错误"),
    VERIFY_IS_BLANK(206,"验证码为空"),
    REGISTER_FAIL(207,"注册失败"),
    EMAIL_OR_PWD_ERROR(208,"邮箱号或密码错误，登录失败"),
    EMAIL_IS_BLANK(209,"邮箱不能为空");

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
