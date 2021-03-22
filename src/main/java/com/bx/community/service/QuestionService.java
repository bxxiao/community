package com.bx.community.service;

import com.bx.community.dto.PaginationDTO;
import com.bx.community.dto.QuestionDTO;
import com.bx.community.dto.QuestionQueryDTO;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.QuestionExtMapper;
import com.bx.community.mapper.QuestionMapper;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.Question;
import com.bx.community.model.QuestionExample;
import com.bx.community.model.User;
import com.bx.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper uMapper;

    @Autowired
    private QuestionMapper qMapper;

    @Autowired
    private QuestionExtMapper qExtMapper;

    /**
     * @param search
     * @param size
     * @param page
     * @return
     */
    public PaginationDTO listQuestion(String search, Integer size, Integer page, String tag) {
        Integer offset = size * (page - 1);

        // 将搜索关键字按空格分开，用|相连，sql中用正则查询
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    // 用|相连
                    .collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO = new PaginationDTO();

        // 查询并设置分页信息
        QuestionQueryDTO queryDTO = new QuestionQueryDTO();
        // 若 search 为空，置 null ，让 mybatis 可以正确拼装 sql 语句
        queryDTO.setSearch(search == null || search.trim().equals("") ? null : search);
        if(StringUtils.isNotBlank(tag)){
            queryDTO.setTag("%" + tag + "%");
        }
        Integer totalCount = qExtMapper.countByQueryDTO(queryDTO);
        // 设置分页信息
        paginationDTO.setPagination(totalCount, page, size);

        // 分页查询数据
        queryDTO.setPage(offset);
        queryDTO.setSize(size);
        List<Question> questions = qExtMapper.selectBySearch(queryDTO);

        // 创建dto
        List<QuestionDTO> questionDTOS = new ArrayList<>();
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

        return paginationDTO;
    }

    public PaginationDTO listByUid(Long userId, Integer page, Integer size) {
        Integer offset = size * (page - 1);
        List<Question> questions = qExtMapper.listByUId(userId, offset, size);
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
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);
        User user = uMapper.selectByPrimaryKey(question.getCreator());
        dto.setUser(user);
        return dto;
    }

    public void insertOrUpdate(Question question) {
        if (question.getId() == null || question.getId() == 0) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            qMapper.insertSelective(question);
        } else {
            question.setGmtModified(question.getGmtCreate());
            int ret = qMapper.updateByPrimaryKeySelective(question);
            //更新失败
            if (ret != 1) {
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
    public void increView(Long qid, Long uid) {
        // 访问者是问题发起者，则不增加访问记录（未登录访问也增加（uid 为 null 即表示未登录））
        if(uid != null && qExtMapper.isCreator(qid, uid) > 0){
            return;
        }

        // 在sql中使用 view_count = view_count + 1的方式
        qExtMapper.incView(1, qid);
    }

    public List<QuestionDTO> queryRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }

        String[] split = StringUtils.split(questionDTO.getTag(), ',');
        // 将标签用 | 连接
        String regexpTag = Arrays.stream(split).collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> related = qExtMapper.selectRelated(question);
        List<QuestionDTO> result = related.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q, dto);
            return dto;
        }).collect(Collectors.toList());

        return result;
    }
}
