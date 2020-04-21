package com.ccsu.community.controller;

import com.ccsu.community.cache.HotTagCache;
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
    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "10") Integer size,
                        @RequestParam(name = "search", required = false) String  search,
                        @RequestParam(name = "tag",required = false) String tag,
                        @RequestParam(name = "sort",required = false) String sort) {

        PaginationDTO pagination = questionService.list(search,tag,sort,page, size);
        //排序后的热门标签
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        model.addAttribute("hotTags",hotTags);
        model.addAttribute("tag",tag);
        model.addAttribute("sort",sort);
        return "index";
    }
}
