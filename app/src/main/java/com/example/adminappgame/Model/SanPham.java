package com.example.adminappgame.Model;

public class SanPham {
    private String tenSanPham;
    private int gia;
    private int maLoai;
    private String anhSP;
    private String moTa;
    private String dungLuong;
    private int soLuongTai;

    public int getSoLuongTai() {
        return soLuongTai;
    }

    public void setSoLuongTai(int soLuongTai) {
        this.soLuongTai = soLuongTai;
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



    public SanPham() {
    }

    public SanPham(String tenSanPham, int gia, int maLoai,int soLuongTai,  String dungLuong,String moTa, String anhSP) {
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.maLoai = maLoai;
        this.soLuongTai = soLuongTai;
        this.anhSP = anhSP;
        this.moTa = moTa;
        this.dungLuong = dungLuong;
    }
}
