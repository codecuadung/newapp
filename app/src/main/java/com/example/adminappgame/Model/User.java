package com.example.adminappgame.Model;

public class User {
    private String email;
    private String name;
    private int money;
    private String profileImg;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String email, String name, int money, String profileImg, String password) {
        this.email = email;
        this.name = name;
        this.money = money;
        this.profileImg = profileImg;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public User(String email, String name, int money, String profileImg) {
        this.email = email;
        this.name = name;
        this.money = money;
        this.profileImg = profileImg;
    }

    public User() {
    }
}
