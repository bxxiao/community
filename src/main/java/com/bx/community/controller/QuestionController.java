package com.bx.community.controller;

import com.bx.community.dto.CommentDTO;
import com.bx.community.dto.QuestionDTO;
import com.bx.community.eums.CommentTypeEnum;
import com.bx.community.model.User;
import com.bx.community.service.CommentService;
import com.bx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /**
     * 跳转问题页面
     */
    @GetMapping("/question/{qid}")
    public String question(@PathVariable Long qid, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        Long uid = user == null ? null : user.getId();
        // 先增加再获取dto
        questionService.increView(qid, uid);
        QuestionDTO questionDTO = questionService.getById(qid);
        List<QuestionDTO> relatedQuestions = questionService.queryRelated(questionDTO);

        List<CommentDTO> commentDTOS = commentService.listByTargetId(qid, CommentTypeEnum.QUESTION);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
