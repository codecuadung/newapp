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

import com.example.adminappgame.Model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dang_ky_admin extends AppCompatActivity {
    private EditText edAdmin, edPass, edRePass;
    private Button btnDangKy;

    private FirebaseAuth mAuth;
    private DatabaseReference adminRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_admin);
        mAuth = FirebaseAuth.getInstance();
        adminRef = FirebaseDatabase.getInstance().getReference("admin");

        edAdmin = findViewById(R.id.edAdmin);
        edPass = findViewById(R.id.edPass);
        edRePass = findViewById(R.id.edRePass);
        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edAdmin.getText().toString().trim();
                String password = edPass.getText().toString().trim();
                String confirmPassword = edRePass.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Dang_ky_admin.this, "Vui lòng không bỏ trống thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Dang_ky_admin.this, "Email sai định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Dang_ky_admin.this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  String adminemail = mAuth.getCurrentUser().getUid();
                                  Admin admin = new Admin(email,password);
                                  adminRef.child(adminemail).setValue(admin);
                                  Toast.makeText(Dang_ky_admin.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                  startActivity(new Intent(Dang_ky_admin.this,Dang_Nhap_admin.class));
                              }else {
                                  Toast.makeText(Dang_ky_admin.this,"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                              }
                            }
                        });

            }
        });
    }
}