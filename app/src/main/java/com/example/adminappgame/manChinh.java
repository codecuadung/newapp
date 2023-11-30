package com.example.adminappgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.adminappgame.fragments.FragmentDanhSach;
import com.example.adminappgame.fragments.FragmentSanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class manChinh extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_chinh);
        bottomNavigationView = findViewById(R.id.bottomNavigation);


        Fragment defaultFragment = new FragmentSanPham();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, defaultFragment)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.spham) {
                    fragment = new FragmentSanPham();
                } else {
                    fragment = new FragmentDanhSach();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
                return true;
            }
        });
    }
}
