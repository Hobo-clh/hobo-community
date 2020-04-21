package com.ccsu.community.service;

import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    UserMapper userMapper;

//    public boolean checkLogin(String userName, String password, HttpServletResponse response) {
//        UserExample example = new UserExample();
//        example.createCriteria()
//                .andLoginNameEqualTo(userName)
//                .andPwdEqualTo(password);
//        List<User> users = userMapper.selectByExample(example);
//        if (users.size()==1){
//            response.addCookie(new Cookie("token", users.get(0).getToken()));
//            return true;
//        }
//        return false;
//    }

    public Object login(User user,HttpServletResponse response) {
        String email = user.getAccountId();
        String password = user.getPwd();
        if (email==null||password==null){
            return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_OR_PWD_BLANK);
        }
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(email)
                .andPwdEqualTo(password);
        List<User> users = userMapper.selectByExample(example);
        if (users.size()==1){
            response.addCookie(new Cookie("token", users.get(0).getToken()));
            return ResultDTO.info(200,"登陆成功");
        }
        return ResultDTO.errorOf(CustomizeErrorCode.EMAIL_OR_PWD_ERROR);
//    }
    }
}
