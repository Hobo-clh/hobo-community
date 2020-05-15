package com.ccsu.community.controller;

import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.model.User;
import com.ccsu.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 华华
 * @date 20/05/14
 **/
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/user")
    public String updateUser(@RequestParam String loginName,
                             @RequestParam String bio,
                             HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null) {
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        userService.updateInform(user.getId(),loginName,bio);
        return "redirect:/profile/people";
    }
}
