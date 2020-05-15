package com.ccsu.community.service;

import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 华华
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtCreate(System.currentTimeMillis());
            updateUser.setGmtModified(dbUser.getGmtCreate());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            updateUser.setLoginName(user.getLoginName());
            UserExample userExample2 = new UserExample();

            userExample2.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, userExample2);
        }
    }

    /**
     * 更新头像
     *
     * @param user
     * @param avatarUrl
     */
    public void updateAvatar(User user, String avatarUrl) {
        User updateUser = new User();
        updateUser.setAvatarUrl(avatarUrl);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(user.getId());
        userMapper.updateByExampleSelective(updateUser, example);
    }

    /**
     * 更新名称和签名
     *
     * @param id
     * @param loginName
     * @param bio
     */
    public void updateInform(Long id, String loginName, String bio) {
        User updateUser = new User();
        updateUser.setLoginName(loginName);
        updateUser.setBio(bio);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(id);
        userMapper.updateByExampleSelective(updateUser, example);
    }
}
