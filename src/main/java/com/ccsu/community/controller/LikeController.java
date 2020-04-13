package com.ccsu.community.controller;

import com.ccsu.community.dto.ResultDTO;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.model.LikeUser;
import com.ccsu.community.model.Notification;
import com.ccsu.community.model.User;
import com.ccsu.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @ResponseBody
    @PostMapping("/like")
    public Object like(@RequestBody Notification notification,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        if (notification.getOuterid()==null){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        notification.setNotifier(user.getId());
        boolean flag = likeService.updateLikeCount(notification);
        if (!flag){
            return ResultDTO.errorOf(CustomizeErrorCode.LIKE_FAILURE);
        }
        return ResultDTO.likeOkOf();
    }

    @ResponseBody
    @PostMapping("/judgeLike")
    public Object judgeLike(@RequestBody Notification notification,
                            HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(0,"游客状态");
        }
        notification.setNotifier(user.getId());
        boolean flag = likeService.judgeLike(notification);
        if (flag){
            //用户点赞了评论
            return ResultDTO.likeOkOf();
        }
        return ResultDTO.errorOf(0,"没有点赞");
    }
}
