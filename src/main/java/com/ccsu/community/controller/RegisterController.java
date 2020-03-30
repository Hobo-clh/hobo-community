package com.ccsu.community.controller;

import com.ccsu.community.dto.MyUserDTO;
import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.service.RegisterService;
import com.ccsu.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @RequestMapping("/register")
    public String goRegister(){
        return "register";
    }

//    @PostMapping("/register")
//    public String doRegister(@RequestParam String loginName,
//                             @RequestParam String password,
//                             Model model){
//        if (StringUtils.isNotBlank(loginName)&&StringUtils.isNotBlank(password)){
//            boolean flag = registerService.doRegister(loginName,password);
//            if (flag){
//                model.addAttribute("message","注册成功，快去登录吧~");
//                return "register";
//            }else {
//                model.addAttribute("message","注册失败~~");
//            }
//        }else if (StringUtils.isBlank(loginName)){
//            model.addAttribute("message","用户名不能为空！");
//        }else if (StringUtils.isBlank(password)){
//            model.addAttribute("message","密码不能为空！");
//        }
//        return "register";
//    }

    @ResponseBody
    @PostMapping("/register")
    public Object doRegister(@RequestBody MyUserDTO myUserDTO,
                          HttpServletResponse response){
        String loginName = myUserDTO.getLoginName();
        String password = myUserDTO.getPassword();
        if (StringUtils.isNotBlank(loginName)&&StringUtils.isNotBlank(password)) {
            boolean flag = registerService.doRegister(loginName, password);
            if (flag){
                return ResultDTO.errorOf(200,"注册成功");
            }
            return ResultDTO.errorOf(203,"注册失败，用户名可能已存在");
        }
        return ResultDTO.errorOf(204,"用户名或者密码不能为空！");
    }
}
