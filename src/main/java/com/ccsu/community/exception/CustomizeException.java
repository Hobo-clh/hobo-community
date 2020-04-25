package com.ccsu.community.exception;

/**
 * @author 华华
 */
public class CustomizeException extends RuntimeException {

    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }

    public CustomizeException(String message){
        this.message=message;

    }

    @Override
    public String getMessage(){
        return message;
    }

    public Integer getCode(){
        return code;
    }
}
