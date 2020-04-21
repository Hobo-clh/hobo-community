package com.ccsu.community.controller;


import com.ccsu.community.model.User;
import com.ccsu.community.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

//    @ResponseBody
//    @PostMapping("/login")
//    public Object doLogin(@RequestBody MyUserDTO myUserDTO,
//                          HttpServletResponse response){
//        if (myUserDTO!=null){
//            if (StringUtils.isNotBlank(myUserDTO.getLoginName())&&StringUtils.isNotBlank(myUserDTO.getPassword())){
//                boolean flag = loginService.checkLogin(myUserDTO.getLoginName(),myUserDTO.getPassword(),response);
//                if (flag){
//                    return ResultDTO.loginOkOf();
//                }
//                return ResultDTO.errorOf(202,"用户名或者密码错误！");
//            }
//        }
//        return ResultDTO.errorOf(201,"用户名或密码不能为空！");
//    }


//    public String goLogin(){
//        return "login";
//    }
    @RequestMapping("/login")
    public String goLogin(){
        return "loginRegisterForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public Object login(User user, HttpServletResponse response){
        System.out.println(user);
        return loginService.login(user,response);
    }


}

