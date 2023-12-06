package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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
    EditText edtenSP, edGia, edMaLoai, edDungLuong, edMoTa, edLinkAnh, edSoLuongTai;
    Button btnSave, btnCancel;
    adapterSanPham adapter;
    private List<SanPham> sanPhamList;
    private FirebaseFirestore firestore;
    //để add collection
    private CollectionReference sanPhamCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_san_pham, container, false);
        firestore = FirebaseFirestore.getInstance();

        //truy cập collection
        sanPhamCollection = firestore.collection("games");
        rcv = view.findViewById(R.id.rcvSP);
        sanPhamList = new ArrayList<>();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);
        adapter = new adapterSanPham(sanPhamList);
        rcv.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new adapterSanPham.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
        //update
        adapter.setOnUpdateClickListener(new adapterSanPham.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(int position) {
                updatedialog(position);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInsert();
            }
        });
        sanPhamCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                sanPhamList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    SanPham sanPham = documentSnapshot.toObject(SanPham.class);
                    String documentID = documentSnapshot.getId();
                    if (documentID != null) {
                        sanPham.setDocumentId(documentID);
                    }
                    sanPhamList.add(sanPham);
                }
                adapter = new adapterSanPham(sanPhamList);
                rcv.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "lỗi", e);
            }
        });
        return view;
    }

    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        thuchienxoa(position);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void thuchienxoa(int position) {
        SanPham sanPhamValue = sanPhamList.get(position);
        String documentID = sanPhamValue.getDocumentId();

        if (documentID != null) {
            sanPhamCollection.document(documentID).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            adapter.deleteItem(position);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Lỗi khi xóa sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e(TAG, "Lỗi: getDocumentId() trả về null");
            Log.d(TAG, "Document ID: " + documentID);
        }
    }

    private void updatedialog(int position) {
        SanPham sanPhamValue = sanPhamList.get(position);
        Dialog dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.opendialog_sanpham);

        edtenSP = dialog1.findViewById(R.id.edTenSP);
        edGia = dialog1.findViewById(R.id.edGiaSP);
        edMaLoai = dialog1.findViewById(R.id.edMaLoai);
        edSoLuongTai = dialog1.findViewById(R.id.edSoLuongTai);
        edDungLuong = dialog1.findViewById(R.id.edDungLuong);
        edMoTa = dialog1.findViewById(R.id.edMoTa);
        edLinkAnh = dialog1.findViewById(R.id.edLinkanh);
        btnSave = dialog1.findViewById(R.id.btnSaveTL);
        btnCancel = dialog1.findViewById(R.id.btnCancelTL);

        edtenSP.setText(sanPhamValue.getName());
        edGia.setText(String.valueOf(sanPhamValue.getPrice()));
        edMaLoai.setText(String.valueOf(sanPhamValue.getType()));
        edSoLuongTai.setText(String.valueOf(sanPhamValue.getDownloaded()));
        edDungLuong.setText(sanPhamValue.getStorage());
        edMoTa.setText(sanPhamValue.getDescription());
        edLinkAnh.setText(sanPhamValue.getImg_url());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSP = edtenSP.getText().toString().trim();
                int giaSP = Integer.parseInt(edGia.getText().toString().trim());
                int maLoai = Integer.parseInt(edMaLoai.getText().toString().trim());
                int soLuongTai = Integer.parseInt(edSoLuongTai.getText().toString().trim());
                String dungLuong = edDungLuong.getText().toString().trim();
                String moTa = edMoTa.getText().toString().trim();
                String linkAnh = edLinkAnh.getText().toString().trim();
                if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(String.valueOf(giaSP)) || TextUtils.isEmpty(String.valueOf(maLoai))
                        || TextUtils.isEmpty(String.valueOf(soLuongTai)) || TextUtils.isEmpty(dungLuong)
                        || TextUtils.isEmpty(moTa) || TextUtils.isEmpty(linkAnh)) {
                    Snackbar.make(view, "Vui lòng điền đầy đủ thông tin", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //update
                sanPhamValue.setName(tenSP);
                sanPhamValue.setPrice(giaSP);
                sanPhamValue.setType(maLoai);
                sanPhamValue.setDownloaded(soLuongTai);
                sanPhamValue.setStorage(dungLuong);
                sanPhamValue.setDescription(moTa);
                sanPhamValue.setImg_url(linkAnh);

                String documentID = sanPhamValue.getDocumentId();
                if (documentID != null) {
                    sanPhamCollection.document(documentID).set(sanPhamValue)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(view, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.e(TAG, "Lỗi: getDocumentId() trả về null");
                    Log.d(TAG, "Document ID: " + documentID);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    private void DialogInsert() {
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

                SanPham sanPham = new SanPham(tenSP, Integer.parseInt(giaSP), Integer.parseInt(maLoai), Integer.parseInt(soluongtai), dungLuong, moTa, linkAnh);
                sanPhamCollection.add(sanPham)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                adapter.notifyDataSetChanged();
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