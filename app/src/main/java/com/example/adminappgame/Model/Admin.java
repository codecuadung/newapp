package com.example.adminappgame.Model;

public class Admin {
    private String email;
    private String matKhau;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Admin(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }

    public Admin() {
    }
}
