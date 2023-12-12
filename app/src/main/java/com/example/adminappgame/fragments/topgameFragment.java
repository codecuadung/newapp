package com.example.adminappgame.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminappgame.Model.TopGame;
import com.example.adminappgame.R;
import com.example.adminappgame.adapters.adapterTopGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class topgameFragment extends Fragment {
    private TextView txtdoanhthu;
    private FirebaseFirestore db;
    private RecyclerView rcvTop;
    private adapterTopGame adapter;
    private List<TopGame> topGamesList;
    private int DoanhThu = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_game, container, false);
        txtdoanhthu = view.findViewById(R.id.txtdoanhthu);
        rcvTop = view.findViewById(R.id.rcvtop);
        db = FirebaseFirestore.getInstance();
        if (adapter == null) {
            adapter = new adapterTopGame(new ArrayList<>());
            rcvTop.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvTop.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gọi phương thức để lấy dữ liệu
        //mé có mỗi cái ví, Quên đổi tên trường trong model trùng vs tên trường trong firebase mà mất cmn 4 tếng.
        loadDoanhThuData();
        loadTopGamesData();
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

    private void loadTopGamesData() {
        // Khởi tạo danh sách để kết hợp dữ liệu từ 3 collection
        topGamesList = new ArrayList<>();

        // Truy vấn dữ liệu từ collection "games"
        fetchDataFromCollection("games");

        // Truy vấn dữ liệu từ collection "PopularGames"
        fetchDataFromCollection("PopularGames");

        // Truy vấn dữ liệu từ collection "NewGames"
        fetchDataFromCollection("NewGames");
    }

    private void fetchDataFromCollection(String collectionName) {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert dữ liệu từ document sang đối tượng TopGame
                                TopGame topGame = document.toObject(TopGame.class);
                                topGamesList.add(topGame);
                            }

                            // Gọi phương thức xử lý khi dữ liệu từ một collection đã được lấy
                            handleDataFromCollection();

                            // Log số lượng phần tử trong topGamesList sau mỗi lần thêm dữ liệu
                            Log.d(TAG, "Number of elements in topGamesList: " + topGamesList.size());
                        } else {
                            Toast.makeText(getContext(), "Lỗi khi lấy dữ liệu từ " + collectionName + ": " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void handleDataFromCollection() {
        Log.d(TAG, "Handling data from collection");

        if (topGamesList.size() == 15) {
            Log.d(TAG, "Size of topGamesList is 15");

            // 2. Kết hợp và sắp xếp dữ liệu
            List<TopGame> sortedList = combineAndSortData(topGamesList);
            Log.d(TAG, "Sorted list size: " + sortedList.size());

            // 3. Chọn 10 sản phẩm có "downloaded" lớn nhất
            List<TopGame> top10Games = getTop10Games(sortedList);
            Log.d(TAG, "Top 10 games size: " + top10Games.size());

            // 4. Hiển thị dữ liệu trên RecyclerView
            updateAdapterWithData(top10Games);
        } else {
            Log.d(TAG, "Size of topGamesList is not 15");
        }
    }


    private List<TopGame> combineAndSortData(List<TopGame> topGamesList) {
        // Kết hợp danh sách từ cả ba collection
        List<TopGame> combinedList = new ArrayList<>();
        combinedList.addAll(topGamesList);
        // Sắp xếp danh sách giảm dần theo số lượng tải
        Collections.sort(combinedList, (g1, g2) -> Integer.compare(g2.getDownloaded(), g1.getDownloaded()));

        return combinedList;
    }

    private List<TopGame> getTop10Games(List<TopGame> topGamesList) {
        // Chọn 10 sản phẩm đầu tiên trong danh sách hoặc toàn bộ danh sách nếu nó có ít hơn 10 phần tử
        int size = Math.min(topGamesList.size(), 10);
        return topGamesList.subList(0, size);
    }

    private void updateAdapterWithData(List<TopGame> newData) {
        // Khởi tạo adapter nếu chưa có
        if (adapter == null) {
            adapter = new adapterTopGame(newData);
            rcvTop.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvTop.setAdapter(adapter);
        } else {
            // Nếu đã có adapter, cập nhật dữ liệu và thông báo thay đổi
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        }
    }
}
