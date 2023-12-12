package com.example.adminappgame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.Model.SanPham;
import com.example.adminappgame.Model.TopGame;
import com.example.adminappgame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterTopGame extends RecyclerView.Adapter<adapterTopGame.TopGameViewHolder> {
    private List<TopGame> gameList;

    public adapterTopGame(List<TopGame> gameList) {
        this.gameList = gameList;
    }
    public void setData(List<TopGame> newData) {
        this.gameList = newData;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TopGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_game, parent, false);
        return new TopGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopGameViewHolder holder, int position) {
        TopGame topGame = gameList.get(position);
        holder.bind(topGame);
        Picasso.get().load(topGame.getImg_url()).into(holder.imgSanPham);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class TopGameViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSanPham;
        private TextView txtTenSP, txtSoLuongTai, txtDungLuong, txtGiaChiTiet, txtMota;
        public TopGameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtDungLuong = itemView.findViewById(R.id.txtDungLuong);
            txtGiaChiTiet = itemView.findViewById(R.id.txtGiaChiTiet);
            txtMota = itemView.findViewById(R.id.txtMota);
            txtSoLuongTai = itemView.findViewById(R.id.txtSoLuongTai);

        }
        public void bind(TopGame sanPham) {
            txtTenSP.setText("Tên: " + sanPham.getName());
            txtDungLuong.setText("Dung lượng: " + sanPham.getStorage());
            txtGiaChiTiet.setText("Giá: " + sanPham.getPrice() + "đ");
            txtMota.setText("Mô tả: " + sanPham.getDescription());
            txtSoLuongTai.setText("Số lượng tải: "+sanPham.getDownloaded());
        }
    }
}
