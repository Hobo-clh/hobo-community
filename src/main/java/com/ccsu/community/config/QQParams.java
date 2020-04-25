package com.ccsu.community.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 华华
 */
@Component
@ConfigurationProperties(prefix = "qq")
@Data
public class QQParams {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
