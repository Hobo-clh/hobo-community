package com.ccsu.community.controller;

import com.ccsu.community.dto.AccessTokenDTO;
import com.ccsu.community.dto.GithubUser;
import com.ccsu.community.mapper.UserMapper;
import com.ccsu.community.model.User;
import com.ccsu.community.model.UserExample;
import com.ccsu.community.provider.GithubProvider;
import com.ccsu.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    UserService userService;

    @Value("${github.client.ids}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        try {
            String accessToken = githubProvider.getAccessToken(accessTokenDTO);
            GithubUser githubUser = githubProvider.getUser(accessToken);

            if (githubUser != null && githubUser.getLogin() != null) {
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setLoginName(githubUser.getLogin());
                System.out.println(user.getLoginName());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setAvatarUrl(githubUser.getAvatar_url());
                userService.createOrUpdate(user);
                response.addCookie(new Cookie("token", token));
                //request.getSession().setAttribute("user",githubUser);


            } else {
                //登录失败，重新登录
                return "redirect:/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    /**
     * 测试
     */
    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo("59512452");
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
            response.addCookie(new Cookie("token", users.get(0).getToken()));
        }

        return "redirect:/";
    }
}
