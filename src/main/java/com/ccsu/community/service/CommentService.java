package com.ccsu.community.service;

import com.ccsu.community.dto.CommentDTO;
import com.ccsu.community.enums.CommentTypeEnum;
import com.ccsu.community.enums.NotificationStatusEnum;
import com.ccsu.community.enums.NotificationTypeEnum;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.mapper.*;
import com.ccsu.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment){
        if(comment.getParentId()==null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //--回复评论--
            //父评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //新增评论数
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);
        }else {
            //--回复问题--
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //增加评论数
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
            //创建通知
            createNotify(comment, question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);

        }

        commentMapper.insert(comment);
        
    }
    //创建通知的方法
    private void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setOuterid(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        //0代表未读
        notification.setStatus(0);
        //通知的人
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByIdAndType(Long id, CommentTypeEnum typeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        //拿到所有的问题的评论,并且按创建时间倒序
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        UserExample userExample = new UserExample();
        List<Long> userIds = new ArrayList<>();
        //获取评论人并转换为map
        userIds.addAll(commentators);
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
