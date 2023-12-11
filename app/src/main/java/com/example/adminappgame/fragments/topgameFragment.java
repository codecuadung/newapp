package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class topgameFragment extends Fragment {
private TextView txtdoanhthu;
private FirebaseFirestore db;
private int DoanhThu = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_game, container, false);
        txtdoanhthu = view.findViewById(R.id.txtdoanhthu);
        db = FirebaseFirestore.getInstance();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gọi phương thức để lấy dữ liệu
        //mé có mỗi cái ví , Quên đổi tên trường trong model trùng vs tên trường trong firebase mà mất cmn 4 tếng.
        loadDoanhThuData();
    }

    private void loadDoanhThuData() {
        db.collection("profit")
                //vì có mỗi 1 document nên nhét luôn id cho nhanh
                .document("dRmF7XZkeuDtn97Y7huo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Lấy giá trị của trường "doanhThu" từ tài liệu profit
                                Long doanhThuValue = document.getLong("doanhThu");
                                if (doanhThuValue != null) {
                                    updateDoanhThuText(doanhThuValue.intValue());
                                }
                            } else {
                                Toast.makeText(getContext(), "Không tìm thấy tài liệu profit", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDoanhThuText(int doanhThuValue) {
        txtdoanhthu.setText("Ví của tôi: " + doanhThuValue + " VNĐ");
    }
}