package com.ccsu.community.controller;


import com.ccsu.community.model.User;
import com.ccsu.community.service.LoginService;
import com.ccsu.community.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 华华
 */
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @RequestMapping("/login")
    public String goLogin(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            return "redirect:/";
        }
        return "loginRegisterForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public Object login(User user, HttpServletResponse response){
        return loginService.login(user,response);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        CookieUtils.removeCookie(response,"token");
        return "redirect:/";
    }

}

