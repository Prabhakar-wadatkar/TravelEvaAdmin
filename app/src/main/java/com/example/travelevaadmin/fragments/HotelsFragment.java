package com.example.travelevaadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.adapter.HotelAdapter;
import com.example.travelevaadmin.model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelsFragment extends Fragment {

    private RecyclerView hotelsRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);

        hotelsRecyclerView = view.findViewById(R.id.hotelsRecyclerView);

        // Initialize hotel list
        hotelList = new ArrayList<>();
        hotelList.add(new Hotel("Hotel Paradise", "A luxurious hotel near the beach.", "₹200", R.drawable.hotel_image_1));
        hotelList.add(new Hotel("Mountain Retreat", "A peaceful retreat in the mountains.", "₹150", R.drawable.hotel_image_2));
        hotelList.add(new Hotel("City Inn", "Affordable and centrally located.", "₹80", R.drawable.hotel_image_3));
        hotelList.add(new Hotel("Hotel Paradise", "A luxurious hotel near the beach.", "₹200", R.drawable.hotel_image_1));
        hotelList.add(new Hotel("Mountain Retreat", "A peaceful retreat in the mountains.", "₹150", R.drawable.hotel_image_2));
        hotelList.add(new Hotel("City Inn", "Affordable and centrally located.", "₹80", R.drawable.hotel_image_3));


        // Set up RecyclerView
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelAdapter = new HotelAdapter(hotelList);
        hotelsRecyclerView.setAdapter(hotelAdapter);

        return view;
    }
}
