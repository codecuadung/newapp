package com.example.adminappgame.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.adminappgame.database.DbHelper;
import com.example.adminappgame.Model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    private SQLiteDatabase db;


    public SanPhamDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(SanPham obj){
        ContentValues values = new ContentValues();
        values.put("maSanPham",obj.getMaSanPham());
        values.put("tenSanPham",obj.getTenSanPham());
        values.put("dungLuong",obj.getDungLuong());
        values.put("gia",obj.getGia());
        values.put("soLuongTai",obj.getSoLuongTai());
        values.put("maLoai",obj.getMaLoai());
        values.put("moTa",obj.getMoTa());
        values.put("anhSP", obj.getAnhSP());
        return db.insert("SanPham",null,values);
    }
    public int update(SanPham obj){
        ContentValues values = new ContentValues();
        values.put("maSanPham",obj.getMaSanPham());
        values.put("tenSanPham",obj.getTenSanPham());
        values.put("dungLuong",obj.getDungLuong());
        values.put("gia",obj.getGia());
        values.put("soLuongTai",obj.getSoLuongTai());
        values.put("maLoai",obj.getMaLoai());
        values.put("moTa",obj.getMoTa());
        values.put("anhSP", obj.getAnhSP());
        return db.update("SanPham",values,"maSanPham=?",new String[]{String.valueOf(obj.getMaSanPham())});
    }
    public int delete(String id){
        return db.delete("SanPham","maSanPham=?",new String[]{id});
    }
    public int deleteloai(int maLoai) {
        return db.delete("SanPham", "maLoai=?", new String[]{String.valueOf(maLoai)});
    }
    @SuppressLint("Range")
    public List<SanPham> getData(String sql, String...selectionArgs){
        List<SanPham>list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            SanPham obj = new SanPham();
            obj.setMaSanPham(c.getInt(c.getColumnIndex("maSanPham")));
            obj.setTenSanPham(c.getString(c.getColumnIndex("tenSanPham")));
            obj.setAnhSP(c.getString(c.getColumnIndex("anhSP")));
            obj.setGia((c.getInt(c.getColumnIndex("gia"))));
            obj.setDungLuong(c.getString(c.getColumnIndex("dungLuong")));
            obj.setMaLoai(c.getInt(c.getColumnIndex("maLoai")));
            obj.setSoLuongTai(c.getInt(c.getColumnIndex("soLuongTai")));
            obj.setMoTa(c.getString(c.getColumnIndex("moTa")));
            list.add(obj);
        }
        return list;
    }

    public List<SanPham>getAll(){
        String sql = "SELECT * FROM SanPham";
        return getData(sql);
    }
    public SanPham getid(String id){
        String sql = "SELECT * FROM SanPham WHERE maSanPham=?";
        List<SanPham>list = getData(sql,id);
        return list.get(0);
    }


}
