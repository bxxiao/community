package com.bx.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String login;
    private Long id;
    private String bio;
    // 通过GitHub接口返回的头像自动是 avatar_url，使用fastjson转换时可以将 _ 转换为驼峰形式
    private String avatarUrl;
}
