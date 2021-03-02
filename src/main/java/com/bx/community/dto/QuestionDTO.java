package com.bx.community.dto;

import com.bx.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;//评论数
    private Integer viewCount;//访问次数
    private Integer likeCount;//喜欢的次数
    private String tag;
    private String description;
    private User user;
}
