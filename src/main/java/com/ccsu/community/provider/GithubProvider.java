package com.ccsu.community.provider;

import com.alibaba.fastjson.JSON;
import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.utils.SecurityCer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

@Component
@Slf4j
public class GithubProvider {

    @Autowired
    SecurityCer securityCer;

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        //忽略证书问题  到时候设置AOP
        try {
            SecurityCer.trustAllHttpsCertificates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        //忽略证书问题  到时候设置AOP

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {

            try {
                SecurityCer.trustAllHttpsCertificates();
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }

}


