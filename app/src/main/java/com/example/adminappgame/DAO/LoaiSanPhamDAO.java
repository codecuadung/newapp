package com.example.adminappgame.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.adminappgame.database.DbHelper;
import com.example.adminappgame.Model.LoaiSanPham;

import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamDAO {
    private SQLiteDatabase db;

    public LoaiSanPhamDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public long insert(LoaiSanPham obj){
        ContentValues values = new ContentValues();
        values.put("tenLoai",obj.getTenLoai());
        values.put("anhTL", obj.getAnhTL());
        return db.insert("LoaiSanPham",null,values);
    }
    public int update(LoaiSanPham obj){
        ContentValues values = new ContentValues();
        values.put("tenLoai",obj.getTenLoai());
        values.put("anhTL", obj.getAnhTL());
        return db.update("LoaiSanPham",values,"maLoai=?",new String[]{String.valueOf(obj.getMaLoai())});
    }
    public int delete(String id) {
        return db.delete("LoaiSanPham", "maLoai=?", new String[]{id});
    }
    @SuppressLint("Range")
    public List<LoaiSanPham> getData(String sql, String...selectionArgs){
        List<LoaiSanPham>list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            LoaiSanPham obj = new LoaiSanPham();
            obj.setMaLoai(c.getInt(c.getColumnIndex("maLoai")));
            obj.setTenLoai(c.getString(c.getColumnIndex("tenLoai")));
            obj.setAnhTL(c.getString(c.getColumnIndex("anhTL")));
            list.add(obj);
        }
        return list;
    }
    public List<LoaiSanPham>getAll(){
        String sql = "SELECT * FROM LoaiSanPham";
        return getData(sql);
    }
    public LoaiSanPham getid(String id){
        String sql = "SELECT * FROM LoaiSanPham WHERE maLoai=?";
        List<LoaiSanPham>list = getData(sql,id);
        return list.get(0);
    }
}
