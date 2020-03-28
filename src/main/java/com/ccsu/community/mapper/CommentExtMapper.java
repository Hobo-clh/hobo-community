package com.ccsu.community.mapper;

import com.ccsu.community.model.Comment;

public interface CommentExtMapper {

    int incCommentCount(Comment record);
}