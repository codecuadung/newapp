package com.example.adminappgame.Model;

public class LoaiSanPham {
    private int maLoai;
    private String tenLoai;
    private String anhTL;

    public String getAnhTL() {
        return anhTL;
    }

    public void setAnhTL(String anhTL) {
        this.anhTL = anhTL;
    }

    public LoaiSanPham(int maLoai, String tenLoai, String anhTL) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.anhTL = anhTL;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public LoaiSanPham(int maLoai, String tenLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }

    public LoaiSanPham() {
    }
}
