package com.example.adminappgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.adminappgame.fragments.sanPhamFragment;
import com.example.adminappgame.fragments.taiKhoanFragment;
import com.example.adminappgame.fragments.theLoaiFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment defaultFragment = new theLoaiFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, defaultFragment)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_TheLoai) {
                    fragment = new theLoaiFragment();
                } else if (item.getItemId() == R.id.nav_home) {
                    fragment = new sanPhamFragment();
                } else if (item.getItemId() == R.id.nav_TaiKhoan) {
                    fragment = new taiKhoanFragment();
                }

                // Kiểm tra xem biến fragment đã được khởi tạo chưa
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit();
                }

                return true;
            }
        });
    }
}