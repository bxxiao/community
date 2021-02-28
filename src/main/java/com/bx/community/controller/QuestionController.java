package com.bx.community.controller;

import com.bx.community.dto.QuestionDTO;
import com.bx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping("/question/{id}")
    public String question(@PathVariable Long id, Model model){
        // 先增加再获取dto
        service.increView(id);
        QuestionDTO dto = service.getById(id);
        model.addAttribute("question", dto);
        return "question";
    }
}
