package com.bx.community.service;

import com.bx.community.cache.TagCache;
import com.bx.community.mapper.CommentMapper;
import com.bx.community.mapper.QuestionMapper;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.CommentExample;
import com.bx.community.model.QuestionExample;
import com.bx.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;


    public long queryQuestionNum() {
        return questionMapper.countByExample(new QuestionExample());
    }

    public long queryCommentNum() {
        return commentMapper.countByExample(new CommentExample());
    }

    public long queryUserNum() {
        return userMapper.countByExample(new UserExample());
    }

    public long queryTagNum() {
        return TagCache.getTagNum();
    }
}
