package com.bx.community.service;

import com.bx.community.mapper.UserMapper;
import com.bx.community.model.User;
import com.bx.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public void createOrUpdate(User user) {
        UserExample example = new UserExample();
        example.or().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = mapper.selectByExample(example);
        if(dbUsers.size()==0){
            mapper.insert(user);
        }else{
            User dbUser = dbUsers.get(0);
            dbUser.setGmtModified(user.getGmtModified());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            mapper.updateByPrimaryKeySelective(dbUser);
        }
    }
}
