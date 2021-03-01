package com.bx.community.controller;

import com.bx.community.dto.CommentCreateDTO;
import com.bx.community.dto.ResultDTO;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.model.Comment;
import com.bx.community.model.User;
import com.bx.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping("/comment")
    @ResponseBody
    public Object doComment(@RequestBody CommentCreateDTO dto, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (dto == null || StringUtils.isBlank(dto.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(dto.getParentId());
        comment.setContent(dto.getContent());
        comment.setType(dto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());

        service.insert(comment);
        return ResultDTO.okOf();
    }
}
