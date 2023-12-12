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
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.adapterNewSanPham;
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

public class newGame_Fragment extends Fragment {
    private RecyclerView rcv;
    private FloatingActionButton fab;
    Dialog dialog;
    EditText edtenSP, edGia, edMaLoai, edDungLuong, edMoTa, edLinkAnh, edSoLuongTai;
    Button btnSave, btnCancel;
    adapterNewSanPham adapter;
    private List<NewGame> newGameList;
    private FirebaseFirestore firestore;
    private CollectionReference newGameCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_game_, container, false);
        firestore = FirebaseFirestore.getInstance();

        //truy cập collection
        newGameCollection = firestore.collection("NewGames");
        rcv = view.findViewById(R.id.rcvNewGame);
        newGameList = new ArrayList<>();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);
        adapter = new adapterNewSanPham(newGameList);
        rcv.setAdapter(adapter);


        adapter.setOnDeleteClickListener(new adapterNewSanPham.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
        //update
        adapter.setOnUpdateClickListener(new adapterNewSanPham.OnUpdateClickListener() {
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
        newGameCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                newGameList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    NewGame newGame = documentSnapshot.toObject(NewGame.class);
                    String documentID = documentSnapshot.getId();
                    if (documentID != null) {
                        newGame.setDocumentId(documentID);
                    }
                    newGameList.add(newGame);
                }
                adapter = new adapterNewSanPham(newGameList);
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
        NewGame newGameValue = newGameList.get(position);
        String documentID = newGameValue.getDocumentId();

        if (documentID != null) {
            newGameCollection.document(documentID).delete()
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
        NewGame newGameValue = newGameList.get(position);
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

        edtenSP.setText(newGameValue.getName());
        edGia.setText(String.valueOf(newGameValue.getPrice()));
        edMaLoai.setText(String.valueOf(newGameValue.getGenre()));
        edSoLuongTai.setText(String.valueOf(newGameValue.getDownloaded()));
        edDungLuong.setText(newGameValue.getStorage());
        edMoTa.setText(newGameValue.getDescription());
        edLinkAnh.setText(newGameValue.getImg_url());
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
                newGameValue.setName(tenSP);
                newGameValue.setPrice(Integer.parseInt(giaSP));
                newGameValue.setGenre(Integer.parseInt(maLoai));
                newGameValue.setDownloaded(Integer.parseInt(soLuongTai));
                newGameValue.setStorage(dungLuong);
                newGameValue.setDescription(moTa);
                newGameValue.setImg_url(linkAnh);

                String documentID = newGameValue.getDocumentId();
                if (documentID != null) {
                    newGameCollection.document(documentID).set(newGameValue)
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
                DocumentReference reference = newGameCollection.document();
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


                NewGame newGame = new NewGame(documentID,tenSP, Integer.parseInt(giaSP), Integer.parseInt(maLoai), dungLuong, moTa, linkAnh, Integer.parseInt(soluongtai));
                newGameCollection.add(newGame)
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