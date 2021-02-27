package com.bx.community.service;

import com.bx.community.dto.PaginationDTO;
import com.bx.community.dto.QuestionDTO;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.QuestionMapper;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.Question;
import com.bx.community.model.QuestionExample;
import com.bx.community.model.User;
import com.bx.community.model.UserExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper uMapper;

    @Autowired
    private QuestionMapper qMapper;


    public PaginationDTO listQuestion(Integer size, Integer page) {
        Integer offset = size*(page-1);
        List<Question> questions = qMapper.list(offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        UserExample example = new UserExample();
        for (Question question : questions) {
            example.or().andIdEqualTo(question.getCreator());
            User user = uMapper.selectByExample(example).get(0);
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(question, dto);
            dto.setUser(user);
            questionDTOS.add(dto);
        }

        paginationDTO.setData(questionDTOS);
        Integer totalCount = qMapper.count();
        paginationDTO.setPagination(totalCount, page, size);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        Integer offset = size*(page-1);
        List<Question> questions = qMapper.listByUId(userId, offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            User user = uMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(question, dto);
            dto.setUser(user);
            questionDTOS.add(dto);
        }

        paginationDTO.setData(questionDTOS);
        QuestionExample example = new QuestionExample();
        example.or().andCreatorEqualTo(userId);
        Integer totalCount = qMapper.countByExample(example);
        paginationDTO.setPagination(totalCount, page, size);

        return paginationDTO;

    }

    public QuestionDTO getById(Integer id) {
        Question question = qMapper.selectByPrimaryKey(id);
        // 请求的问题可能不存在，抛出异常，跳转至错误页面
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        User user = uMapper.selectByPrimaryKey(question.getCreator());
        dto.setUser(user);
        return dto;
    }

    public void insertOrUpdate(Question question) {
        if(question.getId()==null || question.getId() == 0){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            qMapper.insert(question);
        }else {
            question.setGmtModified(question.getGmtCreate());
            int ret = qMapper.updateByPrimaryKeySelective(question);
            //更新失败
            if(ret!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public Question selectById(Integer id) {
        return qMapper.selectByPrimaryKey(id);
    }
}
