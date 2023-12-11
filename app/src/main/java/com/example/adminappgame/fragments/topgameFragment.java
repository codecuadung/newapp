package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminappgame.R;

public class topgameFragment extends Fragment {
private TextView txtdoanhthu;
private int DoanhThu = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_game, container, false);
        txtdoanhthu = view.findViewById(R.id.txtdoanhthu);
        txtdoanhthu.setText("Doanh thu: " + DoanhThu + " VND");

        return view;
    }
    public void updateDoanhThu(int rechargeMoney) {
        if (getContext() != null) {
            DoanhThu += rechargeMoney;
            txtdoanhthu.setText("Doanh thu: " + DoanhThu + " VND");
            Toast.makeText(getContext(), "Doanh Thu"+DoanhThu, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "nạp"+rechargeMoney, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "topgameFragment: " + DoanhThu);
        } else {
            Log.e(TAG, "txtDoanhThu is null");
        }
    }

}