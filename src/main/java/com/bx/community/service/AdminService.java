package com.bx.community.service;

import com.bx.community.cache.TagCache;
import com.bx.community.dto.PaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;


    public long queryQuestionNum() {
        return questionService.count();
    }

    public long queryCommentNum() {
        return commentService.count();
    }

    public long queryUserNum() {
        return userService.count();
    }

    public long queryTagNum() {
        return TagCache.getTagNum();
    }

    public PaginationDTO listQuestion(int page) {
        int size = 10;
        PaginationDTO paginationDTO = questionService.listQuestion(null, size, page, null, null);
        return paginationDTO;
    }
}
