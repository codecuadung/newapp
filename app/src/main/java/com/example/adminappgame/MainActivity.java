package com.example.adminappgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.adminappgame.Model.SanPham;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private EditText edsend;
    private Button btnsend;
    SanPham sanPham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        edsend = findViewById(R.id.edsend);
        btnsend = findViewById(R.id.btnsemd);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sanPham = new SanPham(2,"PUBG",300000,1,0,"https://cdn.tgdd.vn/2020/06/content/hinh-nen-pubg-chon-loc-dep-mat-cho-dien-thoai-va-may-anh4-800x450.jpg","Free Fire là tựa game sinh tồn ăn khách nhất 2018","4GB");
                ref.child("sanPham").child(String.valueOf(sanPham.getMaSanPham())).setValue(sanPham);
            }
        });
    }
}