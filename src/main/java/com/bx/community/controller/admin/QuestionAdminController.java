package com.bx.community.controller.admin;

import com.bx.community.dto.ResultDTO;
import com.bx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/question")
public class QuestionAdminController {

    @Autowired
    private QuestionService service;

    @PostMapping("/setTop")
    @ResponseBody
    public ResultDTO setTop(Long questionId, Boolean isTop){
        service.setQuestionTop(questionId, isTop);
        return ResultDTO.okOf();
    }
}
