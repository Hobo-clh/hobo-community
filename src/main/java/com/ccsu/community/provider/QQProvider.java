package com.ccsu.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccsu.community.config.QQParams;
import com.ccsu.community.dto.QQUser;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author song
 * @create 2020/2/14 16:16
 */
@Component
@Slf4j
public class QQProvider {

    @Autowired
    private QQParams qqParams;

    private OkHttpClient client = new OkHttpClient();

    /**
     * 获取AccessToken
     */
    public String getAccessToken(String code) {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=" +
                qqParams.getClientId() +
                "&client_secret=" + qqParams.getClientSecret() +
                "&code=" + code +
                "&redirect_uri=" + qqParams.getRedirectUri();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String res = str.split("&")[0].split("=")[1];
            System.out.println(res);
            return res;
        } catch (Exception e) {
            log.error("获取QQ AccessToken 失败", e);
        }
        return null;
    }

    /**
     * 获取OpenId
     */
    public String getOpenId(String accessToken) {
        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string().split(" ")[1];
            return JSONObject.parseObject(str).getString("openid");
        } catch (Exception e) {
            log.error("获取OpenId失败",e);
        }
        return null;
    }

    /**
     * 根据access_token获取用户信息
     */
    public QQUser getQQUser(String accessToken, String openId) {
        String url = "https://graph.qq.com/user/get_user_info?access_token=" + accessToken +
                "&oauth_consumer_key=" + qqParams.getClientId() +
                "&openid=" + openId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //得到的是json字符串，因此需要转为QQUser对象
            String str = response.body().string();
            return JSON.parseObject(str, QQUser.class);
        } catch (IOException e) {
            log.error("获取QQUser错误",e);
        }
        return null;
    }
}
