package com.ccsu.community.mapper;

import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserExtMapper {
    //增加通知数
    int incNotificationCount(User record);
}