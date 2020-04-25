package com.ccsu.community.service;

import com.ccsu.community.enums.NotificationStatusEnum;
import com.ccsu.community.enums.NotificationTypeEnum;
import com.ccsu.community.mapper.*;
import com.ccsu.community.model.*;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserExtMapper userExtMapper;
    @Autowired
    QuestionMapper questionMapper;

    public boolean updateLikeCount(Notification notification) {

        NotificationExample notificationExample = new NotificationExample();
        Long outerid = notification.getOuterid();
        Integer type = notification.getType();
        notificationExample.createCriteria()
                .andOuteridEqualTo(outerid)
                .andNotifierEqualTo(notification.getNotifier())
                .andTypeEqualTo(type);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        if (notifications.size()!=0){
            //已经点过赞了
            return false;
        }
        Integer update;
        User receiver = null;
        if (type ==NotificationTypeEnum.LIKE_COMMENT.getType()|| type ==NotificationTypeEnum.LIKE_TWO_COMMENT.getType()){
            Comment comment = commentMapper.selectByPrimaryKey(outerid);
            comment.setLikeCount(comment.getLikeCount()+1);
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andIdEqualTo(outerid);
            update = commentMapper.updateByExampleSelective(comment, commentExample);
            if (update==1){
                //成功返回1
                receiver = userMapper.selectByPrimaryKey(comment.getCommentator());
            }
        }
        if (type ==NotificationTypeEnum.LIKE_QUESTION.getType()){
            Question question = questionMapper.selectByPrimaryKey(outerid);
            question.setLikeCount(question.getLikeCount()+1);
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(outerid);
            update = questionMapper.updateByExampleSelective(question, questionExample);
            if (update==1){
                //成功返回1
                receiver = userMapper.selectByPrimaryKey(question.getCreator());
            }
        }
        receiver.setNotificationCount(1);
        userExtMapper.incNotificationCount(receiver);

        notification.setReceiver(receiver.getId());
        notification.setStatus(0);
        notification.setGmtCreate(System.currentTimeMillis());
        int i = notificationMapper.insert(notification);
        return i >= 1;
    }

    public boolean judgeLike(Notification notification) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andNotifierEqualTo(notification.getNotifier())
                .andOuteridEqualTo(notification.getOuterid())
                .andTypeEqualTo(notification.getType());
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        if (notifications.size()!=0){
            //点赞了
            return true;
        }
        return false;
    }
}
