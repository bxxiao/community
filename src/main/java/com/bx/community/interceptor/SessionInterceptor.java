package com.bx.community.interceptor;

import com.bx.community.exception.CustomizeErrorCode;
import com.bx.community.exception.CustomizeException;
import com.bx.community.mapper.UserMapper;
import com.bx.community.model.User;
import com.bx.community.model.UserExample;
import com.bx.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 如果 user 为 null ，从 cookie 获取 token ，尝试查询对应的 user
        if(user == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        UserExample example = new UserExample();
                        example.or().andTokenEqualTo(token);
                        List<User> users = mapper.selectByExample(example);
                        if (users != null && users.size() > 0) {
                            user = users.get(0);
                            session.setAttribute("user", user);
                            // 放置未读通知数量
                            Long unReadCount = notificationService.queryUnReadCount(user.getId());
                            session.setAttribute("unReadCount", unReadCount);
                            return true;
                        }
                    }
                }
            }
        }

        // 若user仍然为空，表示token不正确，抛出异常
        throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
