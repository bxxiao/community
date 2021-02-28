package com.bx.community.controller;

import com.bx.community.dto.CommentDTO;
import com.bx.community.dto.ResultDTO;
import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.mapper.CommentMapper;
import com.bx.community.model.Comment;
import com.bx.community.model.User;
import com.bx.community.service.CommentService;
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
    public Object post(@RequestBody CommentDTO dto, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(dto.getParentId());
        comment.setContent(dto.getContent());
        comment.setType(dto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(1L);

        service.insert(comment);
        return ResultDTO.okOf();
    }
}
