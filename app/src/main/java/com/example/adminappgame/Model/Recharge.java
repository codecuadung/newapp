package com.example.adminappgame.Model;

public class Recharge {
    private String userMail;
    private int userMoney;
    private int moneyRecharge;
    private String currentDate;
    private String currenTime;

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMmail) {
        this.userMail = userMmail;
    }

    public int getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(int userMoney) {
        this.userMoney = userMoney;
    }

    public int getMoneyRecharge() {
        return moneyRecharge;
    }

    public void setMoneyRecharge(int moneyRecharge) {
        this.moneyRecharge = moneyRecharge;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrenTime() {
        return currenTime;
    }

    public void setCurrenTime(String currenTime) {
        this.currenTime = currenTime;
    }

    public Recharge(String userMail, int userMoney, int moneyRecharge, String currentDate, String currenTime) {
        this.userMail = userMail;
        this.userMoney = userMoney;
        this.moneyRecharge = moneyRecharge;
        this.currentDate = currentDate;
        this.currenTime = currenTime;
    }

    public Recharge() {
    }
}
