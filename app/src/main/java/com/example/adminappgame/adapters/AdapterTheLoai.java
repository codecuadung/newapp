package com.example.adminappgame.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.DAO.LoaiSanPhamDAO;
import com.example.adminappgame.DAO.SanPhamDAO;
import com.example.adminappgame.R;
import com.example.adminappgame.database.DbHelper;
import com.example.adminappgame.Model.LoaiSanPham;
import com.example.adminappgame.Model.NguoiDung;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTheLoai extends RecyclerView.Adapter<AdapterTheLoai.TheLoaiViewHolder> {
    private List<LoaiSanPham> listLoaiSanPham;
    private OnItemClickListener onItemClickListener;
    private OnUpdateClickListener onUpdateClickListener;
    SanPhamDAO sanPhamDAO;
    LoaiSanPhamDAO loaiSanPhamDAO;
    NguoiDung nguoiDung;
    DbHelper dbHelper;

    public AdapterTheLoai(List<LoaiSanPham> listLoaiSanPham, Context context) {
        this.listLoaiSanPham = listLoaiSanPham;
        this.dbHelper = new DbHelper(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(LoaiSanPham loaiSanPham);
    }
    public interface OnUpdateClickListener {
        void onUpdateClick(LoaiSanPham loaiSanPham);
    }


    @NonNull
    @Override
    public TheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theloai, parent, false);

        return new TheLoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheLoaiViewHolder holder, int position) {
        LoaiSanPham loaiSanPham = listLoaiSanPham.get(position);
        // Load ảnh từ URL bằng Picasso
        Picasso.get().load(loaiSanPham.getAnhTL()).into(holder.imgTheLoai);

        holder.txtTheLoai.setText(loaiSanPham.getTenLoai());
        holder.imgTheLoai.setOnClickListener(view -> {
            onItemClickListener.onItemClick(loaiSanPham);
        });
        nguoiDung = dbHelper.getNguoiDung("admin");
        if (nguoiDung.getVaiTro() == 1) {
            //Phân quyền
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Hiển thị hộp thoại xác nhận trước khi xóa
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Xác nhận xóa");
                        builder.setMessage("Bạn có chắc chắn muốn xóa loại sản phẩm này và tất cả các sản phẩm thuộc loại đó không?");

                        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sanPhamDAO = new SanPhamDAO(view.getContext());
                                loaiSanPhamDAO = new LoaiSanPhamDAO(view.getContext());
                                // Xóa tất cả các sản phẩm thuộc loại đó
                                sanPhamDAO.deleteloai(loaiSanPham.getMaLoai());

                                // Xóa loại sản phẩm
                                loaiSanPhamDAO.delete(String.valueOf(loaiSanPham.getMaLoai()));

                                // Sau khi xóa, cập nhật lại danh sách và thông báo sự thay đổi
                                listLoaiSanPham.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, listLoaiSanPham.size());
                                Toast.makeText(view.getContext(), "Xóa thể loại thành công", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder.show();
                    }
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
        //interface update
        if(nguoiDung.getVaiTro()==1){
            holder.btnUpdate.setVisibility(View.VISIBLE);
            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onUpdateClickListener != null) {
                        onUpdateClickListener.onUpdateClick(loaiSanPham);
                    }
                }
            });
        }else {
            holder.btnUpdate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listLoaiSanPham.size();
    }

    public static class TheLoaiViewHolder extends RecyclerView.ViewHolder {
        public ImageButton imgTheLoai;
        public TextView txtTheLoai;
        public Button btnDelete,btnUpdate;

        public TheLoaiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTheLoai = itemView.findViewById(R.id.imgTheLoai);
            txtTheLoai = itemView.findViewById(R.id.txtTheLoai);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
