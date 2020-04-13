package com.ccsu.community.service;

import com.ccsu.community.dto.NotificationDTO;
import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.enums.NotificationStatusEnum;
import com.ccsu.community.enums.NotificationTypeEnum;
import com.ccsu.community.mapper.CommentMapper;
import com.ccsu.community.mapper.NotificationMapper;
import com.ccsu.community.mapper.QuestionMapper;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PaginationDTO list(Long id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        //查看页数是否合法
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        Integer offset = size * (page - 1);
        if (offset < 0) {
            offset = 0;
        }
        //把当前用户的发出的问题拿到并加入DTO中
        NotificationExample example = new NotificationExample();
        example.setOrderByClause("gmt_create desc");
        example.createCriteria().
                andReceiverEqualTo(id);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            User notifier = userMapper.selectByPrimaryKey(notification.getNotifier());
            //获取关联对象的id
            Long outerid = notification.getOuterid();
            Question question = null;
            //获取通知所在文章的title
            if (notification.getType()== NotificationTypeEnum.REPLY_QUESTION.getType()){
                //通知格式为 回复问题  直接得到他
                question= questionMapper.selectByPrimaryKey(outerid);
            }
            if (notification.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()||notification.getType()== NotificationTypeEnum.LIKE_QUESTION.getType()){
                //通知格式为 回复评论 或者 点赞评论
                Comment comment = commentMapper.selectByPrimaryKey(outerid);
                //得到被回复的评论的问题
                question = questionMapper.selectByPrimaryKey(comment.getParentId());
            }
            if (notification.getType()== NotificationTypeEnum.LIKE_COMMENT.getType()){
                //通知格式为 点赞二级评论
                Comment comment = commentMapper.selectByPrimaryKey(outerid);
                Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
                question = questionMapper.selectByPrimaryKey(parentComment.getParentId());

            }
            notificationDTO.setOuterId(question.getId());
            notificationDTO.setOuterTitle(question.getTitle());
            notificationDTO.setStatus(notification.getStatus());
            notificationDTO.setType(notification.getType());
            notificationDTO.setGmtCreate(notification.getGmtCreate());
            notificationDTO.setNotifier(notifier);
            notificationDTOList.add(notificationDTO);
        }


        paginationDTO.setObjectList(notificationDTOList);

        return paginationDTO;
    }

    public void read(User notifier){
        //将未读变为以读
        NotificationExample unReadNotificationExample = new NotificationExample();
        unReadNotificationExample.createCriteria()
                .andStatusEqualTo(0);
        List<Notification> unReadNotificationList = notificationMapper.selectByExample(unReadNotificationExample);
        if(unReadNotificationList.size()!=0){
            for (Notification notification : unReadNotificationList) {
                notification.setStatus(1);
                NotificationExample example1 = new NotificationExample();
                example1.createCriteria()
                        .andIdEqualTo(notification.getId());
                notificationMapper.updateByExampleSelective(notification, example1);
            }
        }
        //通知清0
        notifier.setNotificationCount(0);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdEqualTo(notifier.getId());
        userMapper.updateByExampleSelective(notifier, userExample);
    }

}
