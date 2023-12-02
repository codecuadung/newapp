package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminappgame.Model.SanPham;
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.adapterSanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class sanPhamFragment extends Fragment {
    private RecyclerView rcv;
    private FloatingActionButton fab;
    Dialog dialog;
    EditText edtenSP,edGia,edMaLoai,edDungLuong,edMoTa,edLinkAnh,edSoLuongTai;
    Button btnSave, btnCancel;
    adapterSanPham adapter;
    private FirebaseFirestore firestore;
    private CollectionReference sanPhamCollection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_san_pham, container, false);
        firestore = FirebaseFirestore.getInstance();
        sanPhamCollection = firestore.collection("games");
        rcv = view.findViewById(R.id.rcvSP);
        rcv = view.findViewById(R.id.rcvSP);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInsert();
            }
        });
        sanPhamCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<SanPham> sanPhamList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                updateRecyclerView(sanPhamList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "lỗi", e);
            }
        });
        return view;
    }
    private void updateRecyclerView(List<SanPham> sanPhamList) {
        adapter = new adapterSanPham(sanPhamList);
        rcv.setAdapter(adapter);
    }
    private void DialogInsert(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.opendialog_sanpham);
        edtenSP = dialog.findViewById(R.id.edTenSP);
        edGia = dialog.findViewById(R.id.edGiaSP);
        edMaLoai = dialog.findViewById(R.id.edMaLoai);
        edDungLuong = dialog.findViewById(R.id.edDungLuong);
        edMoTa = dialog.findViewById(R.id.edMoTa);
        edLinkAnh = dialog.findViewById(R.id.edLinkanh);
        edSoLuongTai = dialog.findViewById(R.id.edSoLuongTai);
        btnSave = dialog.findViewById(R.id.btnSaveTL);
        btnCancel = dialog.findViewById(R.id.btnCancelTL);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSP = edtenSP.getText().toString().trim();
                String giaSP = edGia.getText().toString().trim();
                String maLoai = edMaLoai.getText().toString().trim();
                String dungLuong = edDungLuong.getText().toString().trim();
                String moTa = edMoTa.getText().toString().trim();
                String linkAnh = edLinkAnh.getText().toString().trim();
                String soluongtai = edSoLuongTai.getText().toString().trim();

                SanPham sanPham = new SanPham(tenSP,Integer.parseInt(giaSP),Integer.parseInt(maLoai),Integer.parseInt(soluongtai),dungLuong,moTa,linkAnh);
                sanPhamCollection.add(sanPham)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Đóng dialog sau khi thêm thành công
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error adding document", e);
                                Snackbar.make(view, "Lỗi khi thêm sản phẩm: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}