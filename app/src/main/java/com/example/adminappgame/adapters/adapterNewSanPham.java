package com.example.adminappgame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.Model.NewGame;
import com.example.adminappgame.Model.SanPham;
import com.example.adminappgame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterNewSanPham extends RecyclerView.Adapter<adapterNewSanPham.newGameViewholder> {
    private List<NewGame> newGameList;
    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }
    private static adapterNewSanPham.OnUpdateClickListener onUpdateClickListener;

    public void setOnUpdateClickListener(adapterNewSanPham.OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }

    //delete
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private static adapterNewSanPham.OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(adapterNewSanPham.OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public void deleteItem(int position) {
        newGameList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, newGameList.size());
    }
    public adapterNewSanPham(List<NewGame> newGameList) {
        this.newGameList = newGameList;
    }
    @NonNull
    @Override
    public newGameViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_game,parent,false);
        return new newGameViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newGameViewholder holder, int position) {
        NewGame newGame = newGameList.get(position);
        holder.bind(newGame);
        Picasso.get().load(newGame.getImg_url()).into(holder.img_url);
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
        return newGameList.size();
    }

    public static class newGameViewholder extends RecyclerView.ViewHolder {
        private ImageView img_url;
        private TextView txtName, txtStorage, txtDownloaded, txtPrice, txtDescription;
        private Button btndelete,btnupdate;
        public newGameViewholder(@NonNull View itemView) {
            super(itemView);
            img_url = itemView.findViewById(R.id.img_url);
            txtName = itemView.findViewById(R.id.txtName);
            txtStorage = itemView.findViewById(R.id.txtStorage);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDownloaded = itemView.findViewById(R.id.txtDownloaded);
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
        public void bind(NewGame newGame) {
            txtName.setText("Tên: " + newGame.getName());
            txtStorage.setText("Dung lượng: " + newGame.getStorage());
            txtPrice.setText("Giá: " + newGame.getPrice() + "đ");
            txtDescription.setText("Mô tả: " + newGame.getDescription());
            txtDownloaded.setText("Số lượng tải: "+newGame.getDownloaded());
        }
    }

}
