package com.ccsu.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private long id;
    private String bio;
    private String login;
    private String avatar_url;

}
