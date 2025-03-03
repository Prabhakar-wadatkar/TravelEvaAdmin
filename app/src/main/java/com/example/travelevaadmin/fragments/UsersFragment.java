package com.example.travelevaadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelevaadmin.R;
import com.example.travelevaadmin.adapter.UsersAdapter;
import com.example.travelevaadmin.model.All_users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<All_users> userList = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("TravelEvaUsers");

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch user data from Firebase
        fetchUsers();

        return view;
    }

    private void fetchUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear(); // Clear existing data before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    All_users user = snapshot.getValue(All_users.class);
                    userList.add(user);
                }

                // Update the RecyclerView with the new list
                adapter = new UsersAdapter(userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors here
            }
        });
    }
}
