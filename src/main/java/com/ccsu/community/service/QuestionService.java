package com.ccsu.community.service;

import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.dto.QuestionDTO;
import com.ccsu.community.dto.QuestionQueryDTO;
import com.ccsu.community.enums.QuestionTopEnum;
import com.ccsu.community.enums.SortEnum;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.mapper.QuestionExtMapper;
import com.ccsu.community.mapper.QuestionMapper;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.Question;
import com.ccsu.community.model.QuestionExample;
import com.ccsu.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 华华
 */
@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;

    public PaginationDTO list(String search, String tag, String sort, Integer page, Integer size) {
        //page size -->limit page-1,size-->(page-1)*size个数
        //count(1)-->总数-->if 总数%size!=0 则页数为总数/size
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        if (StringUtils.isNotBlank(search)) {
            String[] searches = StringUtils.split(search, " ");
            search = Arrays.stream(searches).collect(Collectors.joining("|"));
            questionQueryDTO.setSearch(search);
        }

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase().equals(sort)) {
                questionQueryDTO.setSort(sort);
                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }
        questionQueryDTO.setSize(size);
        questionQueryDTO.setTag(tag);
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        paginationDTO.setPagination(totalCount, page, size);
        Integer offset = getOffset(paginationDTO.getTotalPage(), page, size);
        questionQueryDTO.setPage(offset);
        List<QuestionDTO> questionDTOs = getSearchQuestionDTOs(questionQueryDTO);
        paginationDTO.setObjectList(questionDTOs);
        return paginationDTO;
    }

    private static Integer getOffset(Integer totalPage, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offset = size * (page - 1);
        return offset;
    }

    private List<QuestionDTO> getSearchQuestionDTOs(QuestionQueryDTO questionQueryDTO) {
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
        return getQuestionDTOS(questionList);
    }

    @NotNull
    private List<QuestionDTO> getQuestionDTOS(List<Question> questionList) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question的属性copy到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    /**
     * 当前用户的所有问题页面
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);
        //查看页数是否合法
        paginationDTO.setPagination(totalCount, page, size);
        //算出offset
        Integer offset = getOffset(paginationDTO.getTotalPage(), page, size);
        //把当前用户的发出的问题拿到并加入DTO中
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().
                andCreatorEqualTo(userId);
        List<QuestionDTO> questionDTOList = getQuestionDTOs(questionExample, offset, size);
        paginationDTO.setObjectList(questionDTOList);
        return paginationDTO;
    }

    private List<QuestionDTO> getQuestionDTOs(QuestionExample questionExample, Integer offset, Integer size) {
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        return getQuestionDTOS(questionList);
    }

    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question questionById = questionMapper.selectByPrimaryKey(id);
        if (questionById == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(questionById.getCreator());
        BeanUtils.copyProperties(questionById, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {

        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0L);
            question.setCommentCount(0L);
            question.setLikeCount(0L);
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(question, example);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public Question getQuestionById(Long id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1L);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        //将标签通过 , 来隔开并存入数组中
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        //标签由 xxx,xxx,xxx形式变为xxx|xxx|xxx形式，用于正则查找
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    public boolean verify(Long id, User user) {
        Question question = questionMapper.selectByPrimaryKey(id);
        boolean flag = question.getCreator().equals(user.getId());
        return flag;
    }

    /**
     * 设置为type设置是否置顶
     * @param id
     */
    public void setTop(Long id, String topStatus) {
        Integer setStatus = null;
        Integer flag = Integer.valueOf(topStatus);
        if (flag==QuestionTopEnum.IS_TOP.getType()){
            setStatus=QuestionTopEnum.NO_TOP.getType();
        }else if (flag==QuestionTopEnum.NO_TOP.getType()){
            setStatus=QuestionTopEnum.IS_TOP.getType();
        }else {
            throw new CustomizeException("置顶类型错误");
        }
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question != null) {
            question.setTop(setStatus);
            questionMapper.updateByPrimaryKeySelective(question);
        }else {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }
}
