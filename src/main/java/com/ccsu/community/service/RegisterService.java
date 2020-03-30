package com.ccsu.community.service;

import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public class RegisterService {

    @Autowired
    UserMapper userMapper;

    //注册
    public boolean doRegister(String userName, String password) {
        User user = new User();
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andLoginNameEqualTo(userName);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()==0) {
            user.setLoginName(userName);
            user.setPwd(password);
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setNotificationCount(0);
            userMapper.insert(user);
            return true;
        }


        return false;

    }
}
