package com.ccsu.community.service;

import com.ccsu.community.dto.PaginationDTO;
import com.ccsu.community.dto.QuestionDTO;
import com.ccsu.community.mapper.QuestionMapper;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.Question;
import com.ccsu.community.model.QuestionExample;
import com.ccsu.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        /**
         *  page size -->limit page-1,size-->(page-1)*size个数
         *  count(1)-->总数-->if 总数%size!=0 则页数为总数/size
         */
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample example1 = new QuestionExample();

        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        //查看页数是否合法
        paginationDTO.setPagination(totalCount,page,size);
        if(page<1){
            page=1;
        }
        if(page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question的属性copy到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionList(questionDTOList);

        return paginationDTO;

    }

    public PaginationDTO list(Integer userId,Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        //查看页数是否合法
        paginationDTO.setPagination(totalCount,page,size);

        if(page<1){
            page=1;
        }
        if(page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }
        Integer offset = size * (page - 1);
        if(offset<0){
            offset=0;
        }
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().
                andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question的属性copy到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionList(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {

        QuestionDTO questionDTO = new QuestionDTO();
        Question questionById = questionMapper.selectByPrimaryKey(id);
        User user = userMapper.selectByPrimaryKey(questionById.getCreator());
        BeanUtils.copyProperties(questionById,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {

        if (question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question, example);
        }
    }

    public Question getQuestionById(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }
}