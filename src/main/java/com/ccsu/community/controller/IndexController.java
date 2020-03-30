package com.ccsu.community.controller;

import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.dto.QuestionDTO;
import com.ccsu.community.mapper.QuestionMapper;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.Question;
import com.ccsu.community.model.User;
import com.ccsu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String  search) {

        PaginationDTO pagination = questionService.list(search,page, size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);

        return "index";
    }
}
