package com.ccsu.community.service;

import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import com.ccsu.community.utils.CodecUtils;
import com.ccsu.community.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

    private static final int COOKIE_EXPIRY = 60 * 60 * 24 * 7;

    public Object login(User user,HttpServletResponse response) {
        String email = user.getAccountId();
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(email);
        List<User> emailUsers = userMapper.selectByExample(userExample);
        if(emailUsers.size()==0){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_NOT_EXIST);
        }

        String password = CodecUtils.md5Hex(user.getPwd(),emailUsers.get(0).getSalt());
        if (email==null||password==null){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_OR_PWD_BLANK);
        }
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(email)
                .andPwdEqualTo(password);
        List<User> users = userMapper.selectByExample(example);
        if (users.size()==1){
            CookieUtils.setCookie(response,"token",users.get(0).getToken(),COOKIE_EXPIRY);
            return ResultDTO.info(200,"登陆成功");
        }
        return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_OR_PWD_ERROR);
    }
}
