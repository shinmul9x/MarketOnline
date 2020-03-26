package com.example.marketonline.database;

import androidx.annotation.NonNull;

public class User {
    private int id;
    private String userName;
    private String password;
    private String avatar;
    private String phoneNumber;
    private String name;

    public User(String userName, String password, String avatar, String phoneNumber, String name) {
        this.userName = userName;
        this.password = password;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public User(int id, String userName, String password, String avatar, String phoneNumber, String name) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.avatar = avatar;
        if (this.avatar == null) {
            this.avatar = "";
        }
        this.phoneNumber = phoneNumber;
        if (this.phoneNumber == null) {
            this.phoneNumber = "";
        }
        this.name = name;
        if (this.name == null) {
            this.name = "";
        }
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.avatar = "";
        this.phoneNumber = "";
        this.name = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + avatar + " " + phoneNumber;
    }
}
