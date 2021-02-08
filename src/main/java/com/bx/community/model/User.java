package com.bx.community.model;

public class User {
    private Integer id;

    private String name;

    private String accountId;

    private String token;

    private Long gmtCreate;

    private Long gmtModified;

    private String bio;

    private String avatarUrl;

    public User(Integer id, String name, String accountId, String token, Long gmtCreate, Long gmtModified, String bio, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
        this.token = token;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}