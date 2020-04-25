package com.ccsu.community.controller;

import com.ccsu.community.dto.CommentDTO;
import com.ccsu.community.dto.QuestionDTO;
import com.ccsu.community.enums.CommentTypeEnum;
import com.ccsu.community.enums.QuestionTopEnum;
import com.ccsu.community.service.CommentService;
import com.ccsu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 华华
 */
@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){

        List<CommentDTO> comments = commentService.listByIdAndType(id, CommentTypeEnum.QUESTION);
        QuestionDTO questionDTO = questionService.getById(id);

        //查找于标签相关的问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        questionService.incView(id);
        model.addAttribute("questionDTO",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }

    /**
     *
     * @param id
     * @param topStatus 当前置顶类型
     * @return
     */
    @PostMapping("/question/{id}/{topStatus}")
    public String setTop(@PathVariable(name = "id") Long id,
                        @PathVariable(name = "topStatus") String topStatus){
        questionService.setTop(id,topStatus);
        return "redirect:/question/"+id;
    }
}
