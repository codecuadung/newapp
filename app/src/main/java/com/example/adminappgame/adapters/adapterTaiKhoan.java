package com.example.adminappgame.adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminappgame.Model.User;
import com.example.adminappgame.R;
import com.example.adminappgame.fragments.taiKhoanFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterTaiKhoan extends RecyclerView.Adapter<adapterTaiKhoan.taiKhoanViewHolder> {
    private List<User> userList;
    private Context context;

    public adapterTaiKhoan(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private static OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    //update money
    public interface OnUpdateMoneyClickListener {
        void onUpdateMoneyClick(int position);
    }

    private static OnUpdateMoneyClickListener onUpdateMoneyClickListener;

    public void setOnUpdateMoneyClickListener(OnUpdateMoneyClickListener listener) {
        this.onUpdateMoneyClickListener = listener;
    }
    //khóa tài khoản
    public interface OnBanStatusClickListener {
        void onBanStatusClick(int position, boolean currentStatus);
    }

    private OnBanStatusClickListener onBanStatusClickListener;

    public void setOnBanStatusClickListener(OnBanStatusClickListener listener) {
        this.onBanStatusClickListener = listener;
    }

    //delete
    public void deleteItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }
    //khóa tài khoản


    @NonNull
    @Override
    public taiKhoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tai_khoan,parent,false);
        return new taiKhoanViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull taiKhoanViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
        Picasso.get().load(user.getProfileImg()).into(holder.imgTaiKhoan);
        //khóa tài khoản
        if (user.isBanStatus()) {
            holder.btnbanStatus.setText("Mở tài khoản");
        } else {
            holder.btnbanStatus.setText("Khóa tài khoản");
        }

        holder.btnbanStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBanStatusClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onBanStatusClickListener.onBanStatusClick(position, user.isBanStatus());
                    }
                }
            }
        });
        holder.btnbanStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBanStatusClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onBanStatusClickListener.onBanStatusClick(position, user.isBanStatus());
                    }
                }
            }
        });

        // Xóa
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class taiKhoanViewHolder extends RecyclerView.ViewHolder{
        private TextView txtEmail,txtTen,txtSoDu;
        private ImageView imgTaiKhoan;
        private Button btndelete,btnupdateMoney,btnbanStatus;
        public taiKhoanViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtSoDu = itemView.findViewById(R.id.txtSoDu);
            imgTaiKhoan = itemView.findViewById(R.id.imgTaiKhoan);
            btndelete = itemView.findViewById(R.id.btndelete);
            btnbanStatus = itemView.findViewById(R.id.btnbanStatus);
            btnupdateMoney = itemView.findViewById(R.id.btnupdateMoney);
            btnupdateMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onUpdateMoneyClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onUpdateMoneyClickListener.onUpdateMoneyClick(position);
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
        public void bind(User user){
            txtEmail.setText("Email: "+user.getEmail());
            txtTen.setText("Tên: "+user.getName());
            txtSoDu.setText("Số dư: "+user.getMoney());
        }
    }
}
