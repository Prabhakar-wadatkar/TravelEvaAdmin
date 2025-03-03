package com.example.travelevaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.model.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each booking
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        // Get the booking data for this position
        Booking booking = bookingList.get(position);

        // Bind the data to the views
        holder.textPlaceName.setText("Place: " + booking.getPlaceName());
        holder.textUserName.setText("Booked by: " + booking.getUserName());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textPlaceName, textUserName;

        public BookingViewHolder(View itemView) {
            super(itemView);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            textUserName = itemView.findViewById(R.id.textUserName);
        }
    }
}
