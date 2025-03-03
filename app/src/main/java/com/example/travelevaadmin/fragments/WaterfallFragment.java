package com.example.travelevaadmin.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travelevaadmin.R;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HillstationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterfallFragment extends Fragment {

    // Arguments for fragment instantiation
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WaterfallFragment() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the fragment with parameters
    public static HillstationFragment newInstance(String param1, String param2) {
        HillstationFragment fragment = new HillstationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_waterfall, container, false);

        // Find the button and set the onClickListener
        MaterialButton buttonViewDetails = rootView.findViewById(R.id.buttonViewDetails);

        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the latitude and longitude of Devkund Waterfall
                double latitude = 18.1667; // Example coordinates for Devkund Waterfall
                double longitude = 73.1667;

                // Create a URI to open the location in Google Maps
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode("Devkund Waterfall"));

                // Create an intent to launch Google Maps
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                // Check if Google Maps is available and start the intent
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getActivity(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
