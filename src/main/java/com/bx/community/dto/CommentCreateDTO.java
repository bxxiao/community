package com.bx.community.dto;

import lombok.Data;

/**
 * 添加评论时的dto
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
