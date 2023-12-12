package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
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

import com.example.adminappgame.Model.NewGame;
import com.example.adminappgame.Model.PopularGame;
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.adapterNewSanPham;
import com.example.adminappgame.adapters.adapterPopularGame;
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
import java.util.List;

public class gamePopular_Fragment extends Fragment {
    private RecyclerView rcv;
    private FloatingActionButton fab;
    Dialog dialog;
    EditText edtenSP, edGia, edMaLoai, edDungLuong, edMoTa, edLinkAnh, edSoLuongTai;
    Button btnSave, btnCancel;
    adapterPopularGame adapter;
    private List<PopularGame> popularGamesList;
    private FirebaseFirestore firestore;
    private CollectionReference popularCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_popular_, container, false);
        firestore = FirebaseFirestore.getInstance();

        //truy cập collection
        popularCollection = firestore.collection("PopularGames");
        rcv = view.findViewById(R.id.rcvpopular);
        popularGamesList = new ArrayList<>();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);
        adapter = new adapterPopularGame(popularGamesList);
        rcv.setAdapter(adapter);


        adapter.setOnDeleteClickListener(new adapterPopularGame.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
        //update
        adapter.setOnUpdateClickListener(new adapterPopularGame.OnUpdateClickListener() {
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
        popularCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                popularGamesList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    PopularGame popularGame = documentSnapshot.toObject(PopularGame.class);
                    String documentID = documentSnapshot.getId();
                    if (documentID != null) {
                        popularGame.setDocumentId(documentID);
                    }
                    popularGamesList.add(popularGame);
                }
                adapter = new adapterPopularGame(popularGamesList);
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
        PopularGame popularGameValue = popularGamesList.get(position);
        String documentID = popularGameValue.getDocumentId();

        if (documentID != null) {
            popularCollection.document(documentID).delete()
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
        PopularGame popularGameValue = popularGamesList.get(position);
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

        edtenSP.setText(popularGameValue.getName());
        edGia.setText(String.valueOf(popularGameValue.getPrice()));
        edMaLoai.setText(String.valueOf(popularGameValue.getGenre()));
        edSoLuongTai.setText(String.valueOf(popularGameValue.getDownloaded()));
        edDungLuong.setText(popularGameValue.getStorage());
        edMoTa.setText(popularGameValue.getDescription());
        edLinkAnh.setText(popularGameValue.getImg_url());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenSP = edtenSP.getText().toString().trim();
                String giaSP = edGia.getText().toString().trim();
                String maLoai = edMaLoai.getText().toString().trim();
                String soLuongTai = edSoLuongTai.getText().toString().trim();
                String dungLuong = edDungLuong.getText().toString().trim();
                String moTa = edMoTa.getText().toString().trim();
                String linkAnh = edLinkAnh.getText().toString().trim();

                if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(giaSP) || TextUtils.isEmpty(String.valueOf(maLoai))
                        || TextUtils.isEmpty(soLuongTai) || TextUtils.isEmpty(dungLuong)
                        || TextUtils.isEmpty(moTa) || TextUtils.isEmpty(linkAnh)) {
                    Snackbar.make(view, "Vui lòng điền đầy đủ thông tin", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // Kiểm tra giá sản phẩm, mã loại và số lượng tải là số dương
                    int gia = Integer.parseInt(giaSP);
                    int maLoaiInt = Integer.parseInt(maLoai);
                    int soLuong = Integer.parseInt(soLuongTai);

                    // Kiểm tra giá sản phẩm, mã loại và số lượng tải là số dương
                    if (gia < 0 || maLoaiInt < 0 || soLuong < 0) {
                        Snackbar.make(view, "Vui lòng nhập số nguyên dương cho giá, mã loại và số lượng tải", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Snackbar.make(view, "Vui lòng nhập số nguyên dương cho giá, mã loại và số lượng tải", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //update
                popularGameValue.setName(tenSP);
                popularGameValue.setPrice(Integer.parseInt(giaSP));
                popularGameValue.setGenre(Integer.parseInt(maLoai));
                popularGameValue.setDownloaded(Integer.parseInt(soLuongTai));
                popularGameValue.setStorage(dungLuong);
                popularGameValue.setDescription(moTa);
                popularGameValue.setImg_url(linkAnh);

                String documentID = popularGameValue.getDocumentId();
                if (documentID != null) {
                    popularCollection.document(documentID).set(popularGameValue)
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
                DocumentReference reference = popularCollection.document();
                String documentID = reference.getId();

                if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(giaSP) || TextUtils.isEmpty(maLoai)
                        || TextUtils.isEmpty(dungLuong) || TextUtils.isEmpty(moTa)
                        || TextUtils.isEmpty(linkAnh) || TextUtils.isEmpty(soluongtai)) {
                    Snackbar.make(view, "Vui lòng điền đầy đủ thông tin", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int gia = Integer.parseInt(giaSP);
                    int maLoaiInt = Integer.parseInt(maLoai);
                    int soLuong = Integer.parseInt(soluongtai);

                    // Kiểm tra giá sản phẩm, mã loại và số lượng tải là số dương
                    if (gia < 0 || maLoaiInt < 0 || soLuong < 0) {
                        Snackbar.make(view, "Vui lòng nhập số nguyên dương cho giá, mã loại và số lượng tải", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Snackbar.make(view, "Vui lòng nhập số nguyên dương cho giá, mã loại và số lượng tải", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                PopularGame popularGame = new PopularGame(documentID,tenSP, Integer.parseInt(giaSP), Integer.parseInt(maLoai), dungLuong, moTa, linkAnh, Integer.parseInt(soluongtai));
                popularCollection.add(popularGame)
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