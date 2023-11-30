package com.example.adminappgame.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.adminappgame.Model.NguoiDung;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "APPBANGAME";
    public static final int DB_VERSION = 1;
    static final String CREATE_TABLE_NGUOI_DUNG = "" +
            "CREATE TABLE NguoiDung (\n" +
            "    maNguoiDung TEXT NOT NULL PRIMARY KEY ,\n" +
            "    tenNguoiDung TEXT NOT NULL,\n" +
            "    matKhau TEXT NOT NULL,\n" +
            "    vaiTro INTEGER CHECK (vaiTro IN (1, 2)) NOT NULL\n" +
            ")";
    //1 :quyền admin
    //2 :quyền user

    static final String CREATE_TABLE_SAN_PHAM = "" +
            "CREATE TABLE SanPham (\n" +
            "    maSanPham INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    tenSanPham TEXT NOT NULL,\n" +
            "    gia INTEGER NOT NULL,\n" +
            "    soLuongTai INTEGER NOT NULL,\n" +
            "    maLoai INTEGER NOT NULL,\n" +
            "    dungLuong TEXT NOT NULL,\n" +
            "    moTa TEXT NOT NULL,\n" +
            "    anhSP TEXT NOT NULL,\n" +
            "    FOREIGN KEY (maLoai) REFERENCES LoaiSanPham(maLoai)\n" +
            ")\n";
    static final String CREATE_TABLE_LOAI_SAN_PHAM = "" +
            "CREATE TABLE LoaiSanPham (\n" +
            "    maLoai INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    tenLoai TEXT NOT NULL,\n" +
            "    anhTL TEXT NOT NULL\n" +
            ")";
    static final String CREATE_TABLE_PHIEU_MUA = "" +
            "CREATE TABLE PhieuMua (\n" +
            "    maPhieuMua INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    maNguoiDung INTEGER NOT NULL,\n" +
            "    maSanPham INTEGER NOT NULL,\n" +
            "    ngay TEXT NOT NULL,\n" +
            "    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung),\n" +
            "    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)\n" +
            ")";
    static final String CREATE_TABLE_GIO_HANG = "" +
            "CREATE TABLE GioHang (\n" +
            "    maGioHang INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    maNguoiDung TEXT NOT NULL,\n" +
            "    maSanPham INTEGER NOT NULL,\n" +
            "    FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung),\n" +
            "    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)\n" +
            ")";
    static final String INSERT_LOAI_SAN_PHAM = "" +
            "INSERT INTO LoaiSanPham(tenLoai,anhTL) VALUES\n" +
            "('Sinh tồn','https://thao68.com/wp-content/uploads/2022/03/hinh-anh-nen-free-fire-max-14.jpg'),\n" +
            "('Chiến thuật','https://thao68.com/wp-content/uploads/2022/03/hinh-anh-nen-free-fire-max-14.jpg')";

    static final String INSERT_SAN_PHAM = "" +
            "INSERT INTO SanPham(tenSanPham,gia,soLuongTai,maLoai,dungLuong,moTa,anhSP) VALUES\n" +
            "('FREE FIRE',20000,0,1,'4GB','Free Fire là tựa game sinh tồn ăn khách nhất 2018','https://cdn-www.bluestacks.com/bs-images/gffm-graphics-comparison-vn-2.jpg'),\n" +
            "('PUBG',100000,0,1,'6GB','PUBG là tựa game sinh tồn số 1 thế giới','https://cdn.tgdd.vn/2020/06/content/hinh-nen-pubg-chon-loc-dep-mat-cho-dien-thoai-va-may-anh4-800x450.jpg'),\n" +
            "('CLASH OF CLANS',50000,0,2,'2GB','Clash of clans là 1 tựa game chiến thuật thú vị','https://i.pinimg.com/736x/b2/6a/81/b26a818b3b44e9cdc5ee2b4ffec09e8c.jpg'),\n" +
            "('PLANT AND ZOOMBIE',50000,0,2,'500MB','Hãy tải ngay tựa game ăn khách nhất, plant and zoombie','https://o.rada.vn/data/image/2016/11/10/PvZ-1-pvz-1.jpg')";
    static final String INSERT_NGUOI_DUNG = ""+
            "INSERT INTO NguoiDung(maNguoiDung,tenNguoiDung,matKhau,vaiTro) VALUES\n"+
            "('admin','admin','admin',1)";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NGUOI_DUNG);
        db.execSQL(CREATE_TABLE_SAN_PHAM);
        db.execSQL(CREATE_TABLE_LOAI_SAN_PHAM);
        db.execSQL(CREATE_TABLE_PHIEU_MUA);
        db.execSQL(CREATE_TABLE_GIO_HANG);
        db.execSQL(INSERT_LOAI_SAN_PHAM);
        db.execSQL(INSERT_SAN_PHAM);
        db.execSQL(INSERT_NGUOI_DUNG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            String dropNguoiDung = "DROP TABLE IF EXISTS NguoiDung";
            db.execSQL(dropNguoiDung);
            String dropSanPham = "DROP TABLE IF EXISTS SanPham";
            db.execSQL(dropSanPham);
            String dropLoaiSanPham = "DROP TABLE IF EXISTS LoaiSanPham";
            db.execSQL(dropLoaiSanPham);
            String dropPhieuMua = "DROP TABLE IF EXISTS PhieuMua";
            db.execSQL(dropPhieuMua);
            String dropGioHang = "DROP TABLE IF EXISTS GioHang";
            db.execSQL(dropGioHang);
        }
    }
    public NguoiDung getNguoiDung(String maNguoiDungstr) {
        SQLiteDatabase db = this.getReadableDatabase();
        NguoiDung nguoiDung = null;

        String[] columns = {"maNguoiDung", "tenNguoiDung", "matKhau", "vaiTro"};
        String selection = "maNguoiDung=?";
        String[] selectionArgs = {maNguoiDungstr};

        Cursor cursor = db.query("NguoiDung", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy thông tin từ cursor và tạo đối tượng người dùng
            int maNguoiDungColumnIndex = cursor.getColumnIndex("maNguoiDung");
            int tenNguoiDungColumnIndex = cursor.getColumnIndex("tenNguoiDung");
            int matKhauColumnIndex = cursor.getColumnIndex("matKhau");
            int vaiTroColumnIndex = cursor.getColumnIndex("vaiTro");

            String maNguoiDung = cursor.getString(maNguoiDungColumnIndex);
            String tenNguoiDung = cursor.getString(tenNguoiDungColumnIndex);
            String matKhau = cursor.getString(matKhauColumnIndex);
            int vaiTro = cursor.getInt(vaiTroColumnIndex);

            nguoiDung = new NguoiDung(maNguoiDung, tenNguoiDung, matKhau, vaiTro);

            cursor.close();
        }

        return nguoiDung;
    }
}
