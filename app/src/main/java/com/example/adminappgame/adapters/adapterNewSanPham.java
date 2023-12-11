//package com.example.adminappgame.adapters;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.adminappgame.R;
//
//public class adapterNewSanPham extends RecyclerView.Adapter<adapterNewSanPham.NewGameViewHolder> {
//
//    @NonNull
//    @Override
//    public NewGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NewGameViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    public static class NewGameViewHolder extends RecyclerView.ViewHolder {
//        ImageView img_url;
//        TextView txtName, txtPrice, txtDownloaded, txtStorage, txtDescription;\
//        Button btnDelete, btnUpdate;
//        public NewGameViewHolder(@NonNull View itemView) {
//            super(itemView);
//            img_url = itemView.findViewById(R.id.img_url);
//            txtName = itemView.findViewById(R.id.txtName);
//            txtPrice = itemView.findViewById(R.id.txtPrice);
//            txtDownloaded = itemView.findViewById(R.id.txtDownloaded);
//            txtStorage = itemView.findViewById(R.id.txtStorage);
//            txtDescription = itemView.findViewById(R.id.txtDescription);
//            btnDelete = itemView.findViewById(R.id.btndelete);
//            btnUpdate = itemView.findViewById(R.id.btnUpdate);
//        }
//    }
//}
