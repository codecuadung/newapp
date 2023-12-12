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
import com.example.adminappgame.Model.PopularGame;
import com.example.adminappgame.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterPopularGame extends RecyclerView.Adapter<adapterPopularGame.popularGameViewHolder> {
    private List<PopularGame> popularGamesList;
    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }
    private static adapterPopularGame.OnUpdateClickListener onUpdateClickListener;

    public void setOnUpdateClickListener(adapterPopularGame.OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }

    //delete
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private static adapterPopularGame.OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(adapterPopularGame.OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public void deleteItem(int position) {
        popularGamesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, popularGamesList.size());
    }
    public adapterPopularGame(List<PopularGame> popularGamesList) {
        this.popularGamesList = popularGamesList;
    }
    @NonNull
    @Override
    public popularGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_game,parent,false);
        return new popularGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull popularGameViewHolder holder, int position) {
        PopularGame popularGame = popularGamesList.get(position);
        holder.bind(popularGame);
        Picasso.get().load(popularGame.getImg_url()).into(holder.img_url);
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
        return popularGamesList.size();
    }

    public static class popularGameViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_url;
        private TextView txtName, txtStorage, txtDownloaded, txtPrice, txtDescription;
        private Button btndelete,btnupdate;
        public popularGameViewHolder(@NonNull View itemView) {
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
        public void bind(PopularGame popularGame) {
            txtName.setText("Tên: " + popularGame.getName());
            txtStorage.setText("Dung lượng: " + popularGame.getStorage());
            txtPrice.setText("Giá: " + popularGame.getPrice() + "đ");
            txtDescription.setText("Mô tả: " + popularGame.getDescription());
            txtDownloaded.setText("Số lượng tải: "+popularGame.getDownloaded());
        }
    }
}
