package com.bx.community.interceptor;

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
import java.util.List;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 检查cookie中是否有token，有则尝试查询对应的user，放入session
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    UserExample example = new UserExample();
                    example.or().andTokenEqualTo(token);
                    List<User> users = mapper.selectByExample(example);
                    if (users != null && users.size()>0) {
                        request.getSession().setAttribute("user", users.get(0));
                        // 放置未读通知数量
                        Long unReadCount = notificationService.queryUnReadCount(users.get(0).getId());
                        request.getSession().setAttribute("unReadCount", unReadCount);
                    }
                    break;
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
