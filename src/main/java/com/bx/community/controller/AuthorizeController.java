package com.bx.community.controller;

import com.bx.community.dto.AccessTokenDTO;
import com.bx.community.dto.GithubUser;
import com.bx.community.model.User;
import com.bx.community.provider.GithubProvider;
import com.bx.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider provider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @RequestMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam String state, HttpServletResponse response, HttpSession session){
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setClient_id(clientId);
        dto.setClient_secret(clientSecret);
        dto.setCode(code);
        dto.setRedirect_uri(redirectUri);
        dto.setState(state);
        //通过GitHub app的api获取token
        String token = provider.getAccessToken(dto);
        // 通过token获取用户信息
        GithubUser githubUser = provider.getUser(token);

        if(githubUser!=null && githubUser.getId() != null){
            User user = new User();
            String loginToken = UUID.randomUUID().toString();
            user.setToken(loginToken);
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            // 若已存在对应user，更新部分信息即可；否则插入
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", loginToken));
            session.setAttribute("user", user);
            return "redirect:/";
        }
        else {
            log.error("callback github user failed: {}.", githubUser);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        //删除token cookie，通过设置一个新的 token，将其存活时间设置为0
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
