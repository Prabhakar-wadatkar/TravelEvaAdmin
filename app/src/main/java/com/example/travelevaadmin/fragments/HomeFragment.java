package com.example.travelevaadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.adapter.BookingAdapter;
import com.example.travelevaadmin.model.Booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private DatabaseReference bookingsRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up click listener on the add_place layout
        View addPlaceLayout = view.findViewById(R.id.add_place);
        addPlaceLayout.setOnClickListener(v -> {
            // Replace the current fragment with AddPlaceFragment
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddPlaceFragment());
            transaction.addToBackStack(null); // Add the transaction to the back stack so the user can navigate back
            transaction.commit();
        });

        // Set up click listener for card_bookings
        View cardBookings = view.findViewById(R.id.card_bookings);
        cardBookings.setOnClickListener(v -> {
            // Show the message when card is clicked
            Toast.makeText(getActivity(), "No Bookings", Toast.LENGTH_SHORT).show();
        });

// Set up click listener for "Add Hotels" card
        View addHotelsCard = view.findViewById(R.id.add_hotels);
        addHotelsCard.setOnClickListener(v -> {
            // Open AddHotelFragment when "Add Hotels" is clicked
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new AddHotelFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

// Set up click listener for "Add Flights" card
        View addFlightsCard = view.findViewById(R.id.add_flights);
        addFlightsCard.setOnClickListener(v -> {
            // Open AddFlightsFragment when "Add Flights" is clicked
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new AddFlightsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter with an empty list
        bookingAdapter = new BookingAdapter(getContext(), bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Initialize Firebase Database reference
        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        // Fetch bookings data from Firebase
        fetchBookingsData();
    }

    private void fetchBookingsData() {
        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookingList.clear();

                // First level: Iterate through all users
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Second level: Iterate through all bookings under each user
                    for (DataSnapshot bookingSnapshot : userSnapshot.getChildren()) {
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        if (booking != null) {
                            // Set the user ID from the parent node
                            booking.setUserId(userSnapshot.getKey());
                            bookingList.add(booking);
                        }
                    }
                }

                // Reverse to show latest bookings first
                Collections.reverse(bookingList);
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
