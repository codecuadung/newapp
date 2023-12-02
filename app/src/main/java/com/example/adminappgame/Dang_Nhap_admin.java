package com.example.adminappgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Dang_Nhap_admin extends AppCompatActivity {
    private EditText edAdminDn, edPassDn;
    private Button btnDangNhap, btnDangky;

    private DatabaseReference adminRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_admin);
        adminRef = FirebaseDatabase.getInstance().getReference().child("admin");
        btnDangky = findViewById(R.id.btnDangKy);
        edAdminDn = findViewById(R.id.edAdmindn);
        edPassDn = findViewById(R.id.edPassdn);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edAdminDn.getText().toString().trim();
                String password = edPassDn.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Dang_Nhap_admin.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                }
                adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                                String savedPassword = adminSnapshot.child("matKhau").getValue(String.class);
                                if (password.equals(savedPassword)) {
                                    Toast.makeText(Dang_Nhap_admin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Dang_Nhap_admin.this, MainActivity.class);
                                    intent.putExtra("userEmail", email);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Dang_Nhap_admin.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(Dang_Nhap_admin.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Dang_Nhap_admin.this, "Lỗi đọc dữ liệu từ Database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dang_Nhap_admin.this,Dang_ky_admin.class));
            }
        });
    }
}