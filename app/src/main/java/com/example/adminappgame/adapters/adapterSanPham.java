package com.example.adminappgame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.Model.SanPham;
import com.example.adminappgame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterSanPham extends RecyclerView.Adapter<adapterSanPham.sanPhamViewHolder> {

    private List<SanPham> sanPhamList;

    public adapterSanPham(List<SanPham> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }
    @NonNull
    @Override
    public sanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham,parent,false);
        return new sanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sanPhamViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);
        holder.bind(sanPham);
        Picasso.get().load(sanPham.getAnhSP()).into(holder.imgSanPham);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class sanPhamViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgSanPham;
        private TextView txtTenSP, txtSoLuongTai, txtDungLuong, txtGiaChiTiet, txtMota;


        public sanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtDungLuong = itemView.findViewById(R.id.txtDungLuong);
            txtGiaChiTiet = itemView.findViewById(R.id.txtGiaChiTiet);
            txtMota = itemView.findViewById(R.id.txtMota);
            txtSoLuongTai = itemView.findViewById(R.id.txtSoLuongTai);

        }
        public void bind(SanPham sanPham) {
            txtTenSP.setText("Tên: " + sanPham.getTenSanPham());
            txtDungLuong.setText("Dung lượng: " + sanPham.getDungLuong());
            txtGiaChiTiet.setText("Giá: " + sanPham.getGia() + "đ");
            txtMota.setText("Mô tả: " + sanPham.getMoTa());

        }
    }
}
