package com.ccsu.community.controller;

import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.mapper.NotificationMapper;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.service.NotificationService;
import com.ccsu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){


        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        
        if("questions".equals(action)){
            //展示所有我的问题、提问
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
        }else if("replies".equals(action)){
            //展示所有我的回复
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            PaginationDTO pagination = notificationService.list(user.getId(), page, size);
            notificationService.read(user);
            model.addAttribute("pagination",pagination);
        }


        return "profile";
    }
}
