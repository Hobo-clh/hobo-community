package com.ccsu.community.mapper;

import com.ccsu.community.model.Comment;
import com.ccsu.community.model.CommentExample;
import com.ccsu.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {

    int incCommentCount(Comment record);
}