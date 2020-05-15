package com.ccsu.community.controller;

import com.ccsu.community.config.GithubParams;
import com.ccsu.community.config.QQParams;
import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.dto.QQUser;
import com.ccsu.community.exception.CustomizeErrorCode;
import com.ccsu.community.exception.CustomizeException;
import com.ccsu.community.model.User;
import com.ccsu.community.provider.GithubProvider;
import com.ccsu.community.provider.QQProvider;
import com.ccsu.community.service.UserService;
import com.ccsu.community.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.UUID;

/**
 * @author 华华
 */
@Slf4j
@Controller
public class AuthorizeController {
    @Autowired
    UserService userService;
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    GithubParams githubParams;
    @Autowired
    QQParams qqParams;
    @Autowired
    QQProvider qqProvider;

    private static final int COOKIE_EXPIRY = 60 * 60 * 24 * 7;

    /**
     * github登录发起请求
     *
     * @return
     */
    @GetMapping("/github/oauth")
    public String github(HttpSession session) {

        //github互联中的回调地址
        //用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("githubState", uuid);
        //Step1：获取Authorization Code
        //String url = "https://github.com/login/oauth/authorize?client_id=Iv1.e1445cb4e1c12491&redirect_uri=http://hobosocool.top:81/callback&scope=user&state=1";
        String url = "https://github.com/login/oauth/authorize?&scope=user" +
                "&client_id=" + githubParams.getClientId() +
                "&redirect_uri=" + githubParams.getRedirectUri() +
                "&state=" + uuid;
        return "redirect:" + url;
    }

    /**
     * github回调
     *
     * @param code
     * @param state
     * @param response
     * @return
     */
    @GetMapping("github/callback")
    public String githubCallback(@RequestParam(name = "code") String code,
                                 @RequestParam(name = "state") String state,
                                 HttpServletResponse response,
                                 HttpSession session) {
        String uuid = (String) session.getAttribute("githubState");
        if (uuid != null) {
            if (!uuid.equals(state)) {
                throw new CustomizeException("github,state错误");
            }
        }
        AccessTokenDTO accessTokenDTO = githubProvider.setAccessTokenDTO(githubParams.getClientId(), githubParams.getClientSecret(), githubParams.getRedirectUri(), code, state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null) {
            String token = UUID.randomUUID().toString();
            User user = setUserInfo(token, githubUser.getLogin(), (String.valueOf(githubUser.getId())), githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            //增加cookie
            CookieUtils.setCookie(response, "token", token, COOKIE_EXPIRY);
            return "redirect:/";
        } else {
            log.error("callback get github error,{}", githubUser);
            // 登录失败，重新登录
            throw new CustomizeException(CustomizeErrorCode.LOGIN_CONNECT_ERROR);
        }
    }

    /**
     * QQ登录发起请求
     *
     * @param session
     * @return
     */
    @GetMapping("/qq/oauth")
    public String qq(HttpSession session) {
        //QQ互联中的回调地址
        //用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("qqState", uuid);
        //Step1：获取Authorization Code
        String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code" +
                "&client_id=" + qqParams.getClientId() +
                "&redirect_uri=" + qqParams.getRedirectUri() +
                "&state=" + uuid;
        //# https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101868811&redirect_uri=http://hobosocool.top:81/qq/callback&state=qqlogin
        return "redirect:" + url;
    }

    /**
     * QQ登录回调
     *
     * @param code
     * @param state
     * @param response
     * @param session
     * @return
     */
    @GetMapping("/qq/callback")
    public String qqCallback(@RequestParam("code") String code,
                             @RequestParam("state") String state,
                             HttpServletResponse response,
                             HttpSession session) {
        String uuid = (String) session.getAttribute("qqState");
        if (uuid != null) {
            if (!uuid.equals(state)) {
                throw new CustomizeException("qq,state错误");
            }
        }
        String accessToken = qqProvider.getAccessToken(code);
        String openId = qqProvider.getOpenId(accessToken);
        QQUser qqUser = qqProvider.getQQUser(accessToken, openId);
        if (qqUser != null && qqUser.getRet() == 0) {
            String token = UUID.randomUUID().toString();
            User user = setUserInfo(token, qqUser.getNickname(), "qq-" + openId, qqUser.getFigureurl_qq_1());
            userService.createOrUpdate(user);
            CookieUtils.setCookie(response, "token", token, COOKIE_EXPIRY);
        } else {
            log.error("callback get qq error,{}", qqUser);
            // 登录失败，重新登录
            throw new CustomizeException(CustomizeErrorCode.LOGIN_CONNECT_ERROR);
        }
        return "redirect:/";
    }

    /**
     * 设置用户信息
     *
     * @param token
     * @param name
     * @param accountId
     * @param avatarUrl
     * @return
     */
    private User setUserInfo(String token, String name, String accountId, String avatarUrl) {
        User user = new User();
        user.setToken(token);
        user.setNotificationCount(0);
        user.setLoginName(name);
        user.setAccountId(accountId);
        user.setAvatarUrl(avatarUrl);
        return user;
    }
}
