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


import com.example.adminappgame.Model.DoanhThu;
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

public class taiKhoanFragment extends Fragment implements adapterTaiKhoan.OnBanStatusClickListener {
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
                String userEmail = user.getEmail();
                checkRecharge(userEmail);
            }
        });
        adapter.setOnBanStatusClickListener(this);

        return view;
    }
    private void checkRecharge(String userEmail) {
        collectionReference = db.collection("Recharge");
        com.google.firebase.firestore.Query query = collectionReference.whereEqualTo("userMail", userEmail);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TaiKhoanFragment", "vo roi");
                        Log.d("TaiKhoanFragment", "Recharge data: " + document.getData().toString());
                        boolean isRecharge = document.getBoolean("isRecharge");
                        if (isRecharge == false) {
                            String userMoney = document.getData().get("userMoney").toString();
                            String rechargeMoney = document.getData().get("moneyRecharge").toString();
                            int newUserMoney = Integer.parseInt(userMoney) + Integer.parseInt(rechargeMoney);
                            performUpdateMoney(userEmail, newUserMoney,Integer.parseInt(rechargeMoney));
                        } else {
                            Toast.makeText(getContext(), "Người dùng đã nạp tiền", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    Toast.makeText(getContext(), "Người dùng chưa nạp tiền", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("TaiKhoanFragment", "Error getting recharge data", task.getException());
                }
            }
        });
    }

    private void performUpdateMoney(String userEmail, int newUserMoney, int rechargeMoney) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        // Tìm người dùng với email cụ thể trong Realtime Database
        Query query = userRef.orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Cập nhật giá trị money cho người dùng
                    snapshot.getRef().child("money").setValue(newUserMoney)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Người dùng nạp tiền thành công", Toast.LENGTH_SHORT).show();
                                    updateIsRecharge(userEmail);
                                    updateDoanhThuInFirestore(rechargeMoney);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Người dùng nạp tiền thất bại", Toast.LENGTH_SHORT).show();
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
    private void updateDoanhThuInFirestore(int rechargeMoney) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference profitRef = db.collection("profit").document("dRmF7XZkeuDtn97Y7huo");

        profitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy giá trị cũ của DoanhThu
                        int currentDoanhThu = document.getLong("doanhThu").intValue();
                        // Cập nhật DoanhThu mới
                        int updatedDoanhThu = currentDoanhThu + rechargeMoney;
                        Toast.makeText(getContext(), "Đã thêm " + updatedDoanhThu + " VND vào ví", Toast.LENGTH_SHORT).show();

                        // Tạo một đối tượng ProfitModel mới để cập nhật lên Firestore
                        DoanhThu doanhThu = new DoanhThu();
                        doanhThu.setDoanhThu(updatedDoanhThu);

                        // Thực hiện cập nhật
                        profitRef.set(doanhThu).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TaiKhoanFragment", "DoanhThu updated successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TaiKhoanFragment", "Error updating DoanhThu", e);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "dell vô", Toast.LENGTH_SHORT).show();
                    Log.e("TaiKhoanFragment", "Error getting profit document", task.getException());
                }
            }
        });
    }



    private void updateIsRecharge(String userEmail) {
        collectionReference.whereEqualTo("userMail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update("isRecharge", true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TaiKhoanFragment", "isRecharge updated successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TaiKhoanFragment", "Error updating isRecharge", e);
                                            }
                                        });
                            }
                        } else {
                            Log.e("TaiKhoanFragment", "Error getting recharge data", task.getException());
                        }
                    }
                });
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


    @Override
    public void onBanStatusClick(int position, boolean currentStatus) {
        Log.e("TaiKhoanFragment", "goi roi");
        User user = listUser.get(position);
        String userEmail = user.getEmail();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey(); // Lấy userId từ Realtime Database
                    if (userId != null) {
                        DatabaseReference specificUserRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        // Đảo ngược trạng thái khóa tài khoản
                        specificUserRef.child("banStatus").setValue(!currentStatus)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                                        // Cập nhật danh sách người dùng sau khi thay đổi
                                        userData();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TaiKhoanFragment", "Error getting user data", databaseError.toException());
            }
        });
    }

}
