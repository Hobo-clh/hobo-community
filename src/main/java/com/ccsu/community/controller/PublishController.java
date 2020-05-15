package com.ccsu.community.controller;

import com.ccsu.community.cache.TagCache;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.model.Question;
import com.ccsu.community.model.User;
import com.ccsu.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 华华
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       HttpServletRequest request,
                       Model model){
        //验证是否为当前问题是否存在
        Question verifyQuestion = questionService.getQuestionById(id);
        if (verifyQuestion==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //验证是否为当前问题的创建者
        User user = (User)request.getSession().getAttribute("user");
        if (user==null) {
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        boolean flag = questionService.verify(verifyQuestion,user);
        if (!flag) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_CREATOR_ERROR);
        }
        Question question = questionService.getQuestionById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id",id);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "文章至少要有一个标签");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)){
            model.addAttribute("error", "输入非法标签："+invalid);
            return "publish";
        }


        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setTop(0);
        questionService.createOrUpdate(question);
        model.addAttribute("success", "发布成功！");
        return "redirect:/";
    }
}
