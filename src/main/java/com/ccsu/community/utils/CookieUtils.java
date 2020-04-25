package com.ccsu.community.utils;

import com.ccsu.community.exception.CustomizeException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 添加指定cookie，并指定有效期
     * @param response
     * @param name
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        try{
            //cookie.setDomain(".hobosocool.top");
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
        }catch(Exception e){
            throw new CustomizeException("创造cookie错误");
        }

    }

    /**
     * 清空指定cookie
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
