package com.bx.community.controller;

import com.bx.community.dto.PaginationDTO;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.User;
import com.bx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private QuestionService service;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable String action, Model model, HttpServletRequest request,
                          @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5")Integer size){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
            return "redirect:/";

        if("questions".equals(action)){
            PaginationDTO paginationDTO = service.list(user.getId(), page, size);
            // section 表示当前是哪个模块，有“我的问题”，“最新回复”
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            model.addAttribute("pagination", paginationDTO);
        }else if("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }


        return "profile";
    }

}
