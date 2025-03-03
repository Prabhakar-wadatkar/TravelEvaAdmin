package com.example.travelevaadmin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.travelevaadmin.LoginActivity;
import com.example.travelevaadmin.R;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize SharedPreferences to fetch the logged-in user's data
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Find the TextViews for Name and Email
        TextView profileName = view.findViewById(R.id.profile_name);
        TextView profileEmail = view.findViewById(R.id.profile_email);

        // Fetch the user's information from SharedPreferences
        String userName = sharedPreferences.getString("name", "John Doe"); // Retrieve user name (set default value)
        String userEmail = sharedPreferences.getString("email", "johndoe@example.com"); // Retrieve user email (default)

        // Set the user's name and email to the TextViews
        profileName.setText(userName);
        profileEmail.setText(userEmail);

        // Find the logout button and set an OnClickListener
        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            // Clear user data from SharedPreferences
            clearUserData();

            // Log out the user (optional: clear any session or FirebaseAuth sign out)
            // Example: FirebaseAuth.getInstance().signOut();

            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // Optionally, close the current activity or fragment
            getActivity().finish(); // Close current activity (optional)
        });

        return view;
    }

    // Method to clear the user data from SharedPreferences
    private void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("email");
        editor.remove("name");
        editor.apply(); // Apply changes to SharedPreferences
    }
}
