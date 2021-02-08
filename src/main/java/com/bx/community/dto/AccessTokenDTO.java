package com.bx.community.dto;

import lombok.Data;

/**
 * 封装GitHub账号认证相关数据
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
