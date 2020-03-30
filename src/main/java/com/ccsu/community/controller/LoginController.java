package com.ccsu.community.controller;

import com.ccsu.community.dto.MyUserDTO;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.service.LoginService;
import com.ccsu.community.service.UserService;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

//    @PostMapping("/login")
//    public String doLogin(@RequestParam String userName,
//                          @RequestParam String password,
//                          HttpServletResponse response, Model model){
//        if (StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(password)){
//            boolean flag = loginService.checkLogin(userName,password,response);
//            if (flag){
//                return "redirect:/";
//            }
//            model.addAttribute("loginError","用户名或密码输入错误");
//        }else if (StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
//            model.addAttribute("loginError","用户名或密码不能未空！");
//        }
//        return "login";
//    }

    @ResponseBody
    @PostMapping("/login")
    public Object doLogin(@RequestBody MyUserDTO myUserDTO,
                          HttpServletResponse response){
        if (myUserDTO!=null){
            if (StringUtils.isNotBlank(myUserDTO.getLoginName())&&StringUtils.isNotBlank(myUserDTO.getPassword())){
                boolean flag = loginService.checkLogin(myUserDTO.getLoginName(),myUserDTO.getPassword(),response);
                if (flag){
                    return ResultDTO.loginOkOf();
                }
                return ResultDTO.errorOf(202,"用户名或者密码错误！");
            }
        }
        return ResultDTO.errorOf(201,"用户名或密码不能为空！");
    }

    @RequestMapping("/login")
    public String goLogin(){
        return "login";
    }


}

