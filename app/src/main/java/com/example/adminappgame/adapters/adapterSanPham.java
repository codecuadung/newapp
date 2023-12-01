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
import com.example.adminappgame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterSanPham extends RecyclerView.Adapter<adapterSanPham.sanPhamViewHolder> {

    private List<SanPham> sanPhamList;
    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }
    private static OnUpdateClickListener onUpdateClickListener;

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }

    //delete
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private static OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public void deleteItem(int position) {
        sanPhamList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sanPhamList.size());
    }
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
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class sanPhamViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgSanPham;
        private TextView txtTenSP, txtSoLuongTai, txtDungLuong, txtGiaChiTiet, txtMota;
        private Button btndelete,btnupdate;


        public sanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtDungLuong = itemView.findViewById(R.id.txtDungLuong);
            txtGiaChiTiet = itemView.findViewById(R.id.txtGiaChiTiet);
            txtMota = itemView.findViewById(R.id.txtMota);
            txtSoLuongTai = itemView.findViewById(R.id.txtSoLuongTai);
            btndelete = itemView.findViewById(R.id.btndelete);
            btnupdate = itemView.findViewById(R.id.btnUpdate);

            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onUpdateClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onUpdateClickListener.onUpdateClick(position);
                        }
                    }
                }
            });
            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDeleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onDeleteClickListener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
        public void bind(SanPham sanPham) {
            txtTenSP.setText("Tên: " + sanPham.getTenSanPham());
            txtDungLuong.setText("Dung lượng: " + sanPham.getDungLuong());
            txtGiaChiTiet.setText("Giá: " + sanPham.getGia() + "đ");
            txtMota.setText("Mô tả: " + sanPham.getMoTa());
            txtSoLuongTai.setText("Số lượng tải: "+sanPham.getSoLuongTai());
        }
    }
}
