package com.bx.community.dto;

import com.bx.community.model.User;
import lombok.Data;

/**
 * 返回评论数据给前端的dto
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
