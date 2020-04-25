package com.ccsu.community.provider;

import com.alibaba.fastjson.JSON;
import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.utils.SecurityCer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GithubProvider {

    @Autowired
    SecurityCer securityCer;

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        ////        忽略证书问题  到时候设置AOP
//        HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());
//        try {
//            SecurityCer.trustAllHttpsCertificates();
////            HttpSSLUtils.solveSSL();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            log.error("获取GitHub AccessToken 失败,{}", accessTokenDTO, e);
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
//         忽略证书问题  到时候设置AOP
//        HttpsURLConnection.setDefaultHostnameVerifier(securityCer.getHv());
//        try {
//            SecurityCer.trustAllHttpsCertificates();
////            HttpSSLUtils.solveSSL();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.github.com/user?access_token=" + accessToken)
                .build();
        try (Response response = client.newCall(request).execute()){
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println("---------------");
            System.out.println(githubUser);
            return githubUser;
        } catch (IOException e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }

    public AccessTokenDTO setAccessTokenDTO(String client_id,String client_secret,String redirect_uri,String code,String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClientId(client_id);
        accessTokenDTO.setClientSecret(client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirectUri(redirect_uri);
        accessTokenDTO.setState(state);
        return accessTokenDTO;
    }
}


