package com.bx.community.service;

import com.bx.community.dto.PaginationDTO;
import com.bx.community.dto.QuestionDTO;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.QuestionExtMapper;
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

    @Autowired
    private QuestionExtMapper qExtMapper;


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

    public PaginationDTO list(Long userId, Integer page, Integer size) {
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
        Long totalCount = qMapper.countByExample(example);
        paginationDTO.setPagination(Math.toIntExact(totalCount), page, size);

        return paginationDTO;

    }

    public QuestionDTO getById(Long id) {
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

    public Question selectById(Long id) {
        return qMapper.selectByPrimaryKey(id);
    }

    /**
     *
     */
    public void increView(Long id) {
        // 该方式存在并发问题（丢失修改）
        // Question question = qMapper.selectByPrimaryKey(id);
        // Question update = new Question();
        // update.setId(id);
        // update.setViewCount(question.getViewCount()+1);
        // qMapper.updateByPrimaryKeySelective(update);

        // 在sql中使用 view_count = view_count + 1的方式
        qExtMapper.incView(1, id);
    }
}
