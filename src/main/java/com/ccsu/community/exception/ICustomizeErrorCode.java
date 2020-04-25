package com.ccsu.community.exception;

/**
 * @author 华华
 */
public interface ICustomizeErrorCode {

    /**
     * 获得错误码
     * @return
     */
    Integer getCode();
    /**
     * 获得错误信息
     * @return
     */
    String getMessage();
}
