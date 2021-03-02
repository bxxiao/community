package com.bx.community.controller;

import com.bx.community.cache.TagCache;
import com.bx.community.model.Question;
import com.bx.community.model.User;
import com.bx.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService service;

    @GetMapping("/publish")
    public String publishPage(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    /**
     * 发布或编辑问题
     */
    @PostMapping("/publish")
    public String doPublish(
            String title, String description, String tag,@RequestParam(required = false) Long id,
            HttpServletRequest request, Model model
    ) {
        // 将信息放在model中，字段检验不通过时可以在前端显示
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        if (title == null || "".equals(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)) {
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        service.insertOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Question question = service.selectById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
