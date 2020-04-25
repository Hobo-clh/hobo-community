package com.ccsu.community.dto;

import com.ccsu.community.model.User;
import lombok.Data;

/**
 * @author 华华
 */
@Data
public class NotificationDTO {

    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outerTitle;
    private Integer type;
    private Long outerId;
}
