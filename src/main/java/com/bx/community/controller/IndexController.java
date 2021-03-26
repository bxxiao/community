package com.bx.community.controller;

import com.bx.community.cache.HotTagCache;
import com.bx.community.dto.PaginationDTO;
import com.bx.community.dto.QuestionDTO;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.User;
import com.bx.community.service.NotificationService;
import com.bx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService service;

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private NotificationService notificationService;

    /**
     * 跳转到index时，先判断是否已登录（根据cookie中的token），若已登录，将对应的User对象放到session
     */
    @RequestMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "5") Integer size,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String tag,
                        HttpSession session) {
        // 查询出所有question，用于在首页显示
        PaginationDTO<QuestionDTO> pagination = service.listQuestion(search, size, page, tag);
        List<String> hotTags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("hotTags", hotTags);
        model.addAttribute("tag", tag);

        // // 若已登录，查询未读通知数
        // User user = (User) session.getAttribute("user");
        // if(user != null){
        //     Long unReadCount = notificationService.queryUnReadCount(user.getId());
        //     model.addAttribute("unReadCount", unReadCount);
        // }

        return "index";
    }
}
