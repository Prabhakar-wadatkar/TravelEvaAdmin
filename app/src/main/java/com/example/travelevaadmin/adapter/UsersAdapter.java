package com.example.travelevaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.model.All_users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<All_users> userList;

    public UsersAdapter(List<All_users> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        All_users user = userList.get(position);
        holder.textViewName.setText(user.getName());
        holder.textViewMobileNumber.setText(user.getEmail());  // Assuming email is being used instead of mobile number
        holder.textViewStatus.setText(user.isActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewMobileNumber, textViewStatus;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewMobileNumber = itemView.findViewById(R.id.textViewMobileNumber);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
