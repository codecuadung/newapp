package com.example.adminappgame.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.DAO.LoaiSanPhamDAO;
import com.example.adminappgame.Model.LoaiSanPham;
import com.example.adminappgame.Model.NguoiDung;
import com.example.adminappgame.adapters.AdapterTheLoai;
import com.example.adminappgame.DAO.LoaiSanPhamDAO;
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.AdapterTheLoai;
import com.example.adminappgame.database.DbHelper;
import com.example.adminappgame.Model.LoaiSanPham;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Fragment_TheLoai extends Fragment {
    RecyclerView rcv;
    private AdapterTheLoai adapter;
    private List<LoaiSanPham> listTheloai;
    LoaiSanPham loaiSanPham;
    private LoaiSanPhamDAO dao;
    FloatingActionButton fab;
    Dialog dialog;
    EditText edTenLoai,edAnhTL;
    Button btnSaveTL,btnCancelTL;
    NguoiDung nguoiDung;
    DbHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment__the_loai, container, false);
        rcv = view.findViewById(R.id.recyclerViewTheLoai);
        dbHelper = new DbHelper(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(layoutManager);
        //khởi tạo dao và adapter
        dao = new LoaiSanPhamDAO(getContext());
        listTheloai = new ArrayList<>();
        adapter = new AdapterTheLoai(listTheloai,getContext());

        AdapterTheLoai.OnItemClickListener clickListener = new AdapterTheLoai.OnItemClickListener() {
            @Override
            public void onItemClick(LoaiSanPham loaiSanPham) {
                Fragment_TheLoai.this.onItemClick(loaiSanPham);
            }
        };
        adapter.setOnItemClickListener(clickListener);
        //load dữ liệu từ csdl
        listTheloai.addAll(dao.getAll());
        rcv.setAdapter(adapter);
        //ánh xạ
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phanQuyenInsert();
            }
        });
        adapter.setOnUpdateClickListener(new AdapterTheLoai.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(LoaiSanPham loaiSanPham) {
                update(loaiSanPham);
            }
        });


        return view;
    }
    private void phanQuyenInsert(){
        nguoiDung = dbHelper.getNguoiDung("admin");
        if (nguoiDung.getVaiTro() == 1) {
            // Hiển thị FloatingActionButton
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Gọi phương thức thêm mới ở đây
                    dialogInsert();
                }
            });
        } else {
            // Ẩn FloatingActionButton
            fab.setVisibility(View.GONE);
        }
    }
    private void update(LoaiSanPham loaiSanPham) {
        // Hiển thị dialog cập nhật tương tự như dialog thêm mới
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loai_san_pham_dialog);

        // Ánh xạ các thành phần trong dialog
        edTenLoai = dialog.findViewById(R.id.edTenLoai);
        edAnhTL = dialog.findViewById(R.id.edAnhTL);
        btnSaveTL = dialog.findViewById(R.id.btnSaveTL);
        btnCancelTL = dialog.findViewById(R.id.btnCancelTL);

        // Hiển thị dữ liệu cần cập nhật trong dialog
        edTenLoai.setText(loaiSanPham.getTenLoai());
        edAnhTL.setText(loaiSanPham.getAnhTL());

        btnCancelTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSaveTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu cập nhật từ dialog
                String tenLoai = edTenLoai.getText().toString().trim();
                String anhTL = edAnhTL.getText().toString().trim();

                if (!tenLoai.isEmpty() && !anhTL.isEmpty()) {
                    // Thực hiện cập nhật dữ liệu vào CSDL
                    loaiSanPham.setTenLoai(tenLoai);
                    loaiSanPham.setAnhTL(anhTL);

                    int result = dao.update(loaiSanPham);

                    // Kiểm tra kết quả và cập nhật RecyclerView nếu thành công
                    if (result > 0) {
                        listTheloai.clear();
                        listTheloai.addAll(dao.getAll());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        // Hiển thị thông báo lỗi nếu có
                        Log.e("UpdateError", "Failed to update data");
                    }
                } else {
                    // Hiển thị thông báo nếu có trường không được nhập
                    Log.e("UpdateError", "Fields cannot be empty");
                }
            }
        });
        dialog.show();
    }
    private void dialogInsert(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loai_san_pham_dialog);
        //ánh xạ
        edTenLoai = dialog.findViewById(R.id.edTenLoai);
        edAnhTL = dialog.findViewById(R.id.edAnhTL);
        btnSaveTL = dialog.findViewById(R.id.btnSaveTL);
        btnCancelTL = dialog.findViewById(R.id.btnCancelTL);

        btnCancelTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSaveTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenLoai = edTenLoai.getText().toString().trim();
                String anhTL = edAnhTL.getText().toString().trim();

                if (!tenLoai.isEmpty() && !anhTL.isEmpty()) {
                    // Thực hiện thêm dữ liệu mới vào CSDL
                    LoaiSanPham newLoaiSanPham = new LoaiSanPham();
                    newLoaiSanPham.setTenLoai(tenLoai);
                    newLoaiSanPham.setAnhTL(anhTL);

                    long result = dao.insert(newLoaiSanPham);

                    // Kiểm tra kết quả và cập nhật RecyclerView nếu thành công
                    if (result > 0) {
                        listTheloai.clear();
                        listTheloai.addAll(dao.getAll());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    } else {
                        // Hiển thị thông báo lỗi nếu có
                        Log.e("InsertError", "Failed to insert data");
                    }
                } else {
                    // Hiển thị thông báo nếu có trường không được nhập
                    Log.e("InsertError", "Fields cannot be empty");
                }
            }
        });
        dialog.show();
    }

    public void onItemClick(LoaiSanPham loaiSanPham){
        sanPhamFragment fragmentSanPham = new sanPhamFragment();
        //Truyền dữ liệu theloai
        Bundle bundle = new Bundle();
        bundle.putString("maLoai", String.valueOf(loaiSanPham.getMaLoai()));
        fragmentSanPham.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentSanPham);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}