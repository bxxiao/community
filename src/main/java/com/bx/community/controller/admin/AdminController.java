package com.bx.community.controller.admin;

import com.bx.community.dto.PaginationDTO;
import com.bx.community.exception.AdminException;
import com.bx.community.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    private Map<String, String> sectionMap;
    {
        sectionMap = new HashMap<>();
        sectionMap.put("question", "问题管理");
        sectionMap.put("comment", "评论管理");
        sectionMap.put("tag", "标签管理");
        sectionMap.put("sensitive", "敏感词管理");
        sectionMap.put("users", "用户管理");
    }

    @RequestMapping({"/", "index", "/overview"})
    public String toIndex(Model model){
        long questionNum = service.queryQuestionNum();
        long commentNum = service.queryCommentNum();
        long tagNum = service.queryTagNum();
        long userNum = service.queryUserNum();

        model.addAttribute("section", "overview");
        model.addAttribute("sectionName", "概览");
        model.addAttribute("questionNum", questionNum);
        model.addAttribute("commentNum", commentNum);
        model.addAttribute("tagNum", tagNum);
        model.addAttribute("userNum", userNum);
        return "admin/index";
    }

    @RequestMapping("/{section}")
    public String toPage(Model model, @PathVariable String section,
                         @RequestParam(required = false, defaultValue = "1") Integer page){
        String sectionName;
        if(StringUtils.isBlank(section) || (sectionName = sectionMap.get(section)) == null)
            throw new AdminException();

        switch (section){
            case "question":
                setQuestionData(model, page);
                break;
            default:
                break;
        }

        model.addAttribute("section", section);
        model.addAttribute("sectionName", sectionName);
        return "admin/" + section;
    }

    private void setQuestionData(Model model, Integer page) {
        PaginationDTO paginationDTO = service.listQuestion(page);
        model.addAttribute("pagination", paginationDTO);
    }

    @RequestMapping("/logout")
    public String logout(){
        return "";
    }

}
