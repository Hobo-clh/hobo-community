package com.ccsu.community.dto;
//包名：数据传输模型

import lombok.Data;

/**
 * @author 华华
 */
@Data
public class AccessTokenDTO {
    /**
     *  客户端ID。
     */
    private String clientId;
    /**
     *  客户端密钥
     */
    private String clientSecret;

    /**
     * 作为对步骤 1 的响应而接收的代码
     */
    private String code;
    /**
     * 应用程序中的用户在授权后发送的URL
     */
    private String redirectUri;
    /**
     * 在步骤 1 中提供的不可猜测的随机字符串
     */
    private String state;

}
