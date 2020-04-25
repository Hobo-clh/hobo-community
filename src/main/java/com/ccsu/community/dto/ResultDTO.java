package com.ccsu.community.dto;

import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import lombok.Data;

/**
 * @author 华华
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO init(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setMessage(message);
        resultDTO.setCode(code);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return init(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return init(e.getCode(),e.getMessage());
    }

    public static ResultDTO okOf() {
        return init(200,"请求成功");
    }
    public static <T> ResultDTO okOf(Object t) {
        ResultDTO resultDTO = okOf();
        resultDTO.setData(t);
        return resultDTO;
    }
    public static <T> ResultDTO info(Integer code, String message){
        return init(code,message);
    }
    public static <T> ResultDTO likeOkOf(){
        return init(200,"点赞成功");
    }
}
