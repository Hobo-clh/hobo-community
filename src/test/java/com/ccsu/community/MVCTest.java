package com.ccsu.community;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

/**
 * @author 华华
 * @date 20/06/11
 **/
@WebMvcTest
public class MVCTest {

}

class User{
    private String name;
    private String address;

    public User() {}

    public User(String name, String address) {
        this.name = name;
        this.address = address;
    }
}