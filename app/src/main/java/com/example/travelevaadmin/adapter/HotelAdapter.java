package com.example.travelevaadmin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.model.Hotel;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;

    public HotelAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.nameTextView.setText(hotel.getName());
        holder.descriptionTextView.setText(hotel.getDescription());
        holder.priceTextView.setText(hotel.getPrice());
        holder.hotelImageView.setImageResource(hotel.getImageResId());
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView;
        ImageView hotelImageView;

        public HotelViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.hotelName);
            descriptionTextView = itemView.findViewById(R.id.hotelDescription);
            priceTextView = itemView.findViewById(R.id.hotelPrice);
            hotelImageView = itemView.findViewById(R.id.hotelImage);
        }
    }
}
