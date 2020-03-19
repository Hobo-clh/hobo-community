package com.ccsu.community.controller;

import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.ids}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        try {
            String accessToken = githubProvider.getAccessToken(accessTokenDTO);
            GithubUser githubUser = githubProvider.getUser(accessToken);
            if (githubUser != null) {
                User user = new User();
                user.setToken(UUID.randomUUID().toString());
                user.setLoginName(githubUser.getLogin());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());

                userMapper.insertUser(user);
                //登录成功，写cookie和session
                request.getSession().setAttribute("user",githubUser);

            }else{
                //登录失败，重新登录
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }
}
