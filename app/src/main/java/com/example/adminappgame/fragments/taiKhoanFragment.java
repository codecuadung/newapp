package com.example.adminappgame.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.adminappgame.Model.Recharge;
import com.example.adminappgame.Model.User;
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.adapterSanPham;
import com.example.adminappgame.adapters.adapterTaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class taiKhoanFragment extends Fragment {
    private RecyclerView rcv;
    private List<User> listUser;
    private adapterTaiKhoan adapter;
    private DatabaseReference reference;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tai_khoan, container, false);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        rcv = view.findViewById(R.id.rcvtk);
        listUser = new ArrayList<>();
        adapter = new adapterTaiKhoan(listUser, getContext());
        db = FirebaseFirestore.getInstance();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(adapter);
        userData();
        adapter.setOnDeleteClickListener(new adapterTaiKhoan.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                User user = listUser.get(position);
                showDeleteConfirmationDialog(user.getEmail());
            }
        });
        adapter.setOnUpdateMoneyClickListener(new adapterTaiKhoan.OnUpdateMoneyClickListener() {
            @Override
            public void onUpdateMoneyClick(int position) {
                User user = listUser.get(position);
                Recharge recharge = new Recharge();
                String rechargeId = recharge.getId();
                collectionReference = db.collection("Recharge").document(rechargeId).collection("CurrentUser");
                Log.d("TaiKhoanFragment", rechargeId);

                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Lấy dữ liệu từ mỗi tài liệu trong bộ sưu tập "Recharge/{documentId}/CurrentUser" và log ra
                                Log.d("TaiKhoanFragment", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("TaiKhoanFragment", "Error getting documents", task.getException());
                        }
                    }
                });
            }
        });
        return view;

    }
    
    private void showDeleteConfirmationDialog(String userEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa tài khoản");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản này?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Người dùng đã chọn "Có", thực hiện xóa tài khoản
                deleteAccount(userEmail);
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Người dùng đã chọn "Không", đóng hộp thoại
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteAccount(String userEmail) {
        Query query = reference.orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TaiKhoanFragment", "User deleted successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TaiKhoanFragment", "Error deleting user", e);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TaiKhoanFragment", "Error getting user data", databaseError.toException());
            }
        });
    }

    private void userData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    listUser.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TaiKhoanFragment", "Error getting user data", databaseError.toException());
            }
        });
    }






}
