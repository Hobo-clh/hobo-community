package com.ccsu.community.provider;

import com.alibaba.fastjson.JSON;
import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.utils.SecurityCer;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

@Component
public class GithubProvider {

    @Autowired
    SecurityCer securityCer;



    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws Exception {


        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //        忽略证书问题  到时候设置AOP
        SecurityCer.trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return string.split("&")[0].split("=")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) throws Exception {


        System.out.println(accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        try {
            //忽略证书问题  到时候设置AOP
            SecurityCer.trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println(githubUser);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
