package com.bx.community.provider;

import com.alibaba.fastjson.JSON;
import com.bx.community.dto.AccessTokenDTO;
import com.bx.community.dto.GithubUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO dto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://github.com/login/oauth/access_token";
        String json = JSON.toJSONString(dto);
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //返回的结果类似：
            //access_token=dcc851c53864837494d7f82529237dba244ca797&scope=user&token_type=bearer
            //提取其中的access_token的属性值
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            GithubUser user = JSON.parseObject(resStr, GithubUser.class);
            // GithubUser user = objectMapper.readValue(resStr, GithubUser.class);
            return user;
        } catch (IOException e) {
            System.out.println("error");
        }
        return null;
    }
}
