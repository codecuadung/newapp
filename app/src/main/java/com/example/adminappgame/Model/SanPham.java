package com.example.adminappgame.Model;

public class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private int gia;
    private int maLoai;
    private int soLuongTai;
    private String anhSP;
    private String moTa;
    private String dungLuong;

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getSoLuongTai() {
        return soLuongTai;
    }

    public void setSoLuongTai(int soLuongTai) {
        this.soLuongTai = soLuongTai;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDungLuong() {
        return dungLuong;
    }

    public void setDungLuong(String dungLuong) {
        this.dungLuong = dungLuong;
    }

    public SanPham(int maSanPham, String tenSanPham, int gia, int maLoai, int soLuongTai, String anhSP, String moTa, String dungLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.maLoai = maLoai;
        this.soLuongTai = soLuongTai;
        this.anhSP = anhSP;
        this.moTa = moTa;
        this.dungLuong = dungLuong;
    }

    public SanPham() {
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", gia=" + gia +
                ", maLoai=" + maLoai +
                ", soLuongTai=" + soLuongTai +
                ", anhSP='" + anhSP + '\'' +
                ", moTa='" + moTa + '\'' +
                ", dungLuong='" + dungLuong + '\'' +
                '}';
    }
}
