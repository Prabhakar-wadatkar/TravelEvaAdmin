package com.example.travelevaadmin.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.travelevaadmin.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddFlightsFragment extends Fragment {

    private MaterialCardView selectedCard = null;
    private EditText departureTimeEditText, arrivalTimeEditText, flightNumberEditText, originEditText, destinationEditText, priceEditText;
    private String selectedAirline = "";
    private DatabaseReference databaseReference;

    public AddFlightsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_flights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("flights");

        MaterialCardView emiratesCard = view.findViewById(R.id.emirates_airline);
        MaterialCardView indigoCard = view.findViewById(R.id.indigo_airline);
        MaterialCardView airIndiaCard = view.findViewById(R.id.airindia_airline);
        MaterialCardView qatarCard = view.findViewById(R.id.qatar_airline);

        flightNumberEditText = view.findViewById(R.id.flight_number);
        originEditText = view.findViewById(R.id.origin);
        destinationEditText = view.findViewById(R.id.destination);
        departureTimeEditText = view.findViewById(R.id.depature_time);
        arrivalTimeEditText = view.findViewById(R.id.arrival_time);
        priceEditText = view.findViewById(R.id.price);
        Button submitButton = view.findViewById(R.id.submit_flight_details);

        emiratesCard.setOnClickListener(v -> onCardClicked(emiratesCard, "Emirates"));
        indigoCard.setOnClickListener(v -> onCardClicked(indigoCard, "IndiGo"));
        airIndiaCard.setOnClickListener(v -> onCardClicked(airIndiaCard, "Air India"));
        qatarCard.setOnClickListener(v -> onCardClicked(qatarCard, "Qatar Airways"));

        departureTimeEditText.setOnClickListener(v -> showTimePickerDialog(departureTimeEditText));
        arrivalTimeEditText.setOnClickListener(v -> showTimePickerDialog(arrivalTimeEditText));

        submitButton.setOnClickListener(v -> saveFlightDetails());
    }

    private void onCardClicked(MaterialCardView card, String airline) {
        if (selectedCard != null) {
            selectedCard.setChecked(false);
            selectedCard.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.gray));
            selectedCard.setStrokeWidth(0);
        }

        card.setChecked(true);
        card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.selected_color));
        card.setStrokeWidth(4);
        selectedCard = card;
        selectedAirline = airline;
    }

    private void showTimePickerDialog(EditText timeEditText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (TimePicker view, int selectedHour, int selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeEditText.setText(time);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void saveFlightDetails() {
        String flightNumber = flightNumberEditText.getText().toString();
        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String departureTime = departureTimeEditText.getText().toString();
        String arrivalTime = arrivalTimeEditText.getText().toString();
        String price = priceEditText.getText().toString();

        if (selectedAirline.isEmpty()) {
            Toast.makeText(getContext(), "Please select an airline", Toast.LENGTH_SHORT).show();
            return;
        }
        if (flightNumber.isEmpty() || origin.isEmpty() || destination.isEmpty() || departureTime.isEmpty() || arrivalTime.isEmpty() || price.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String flightId = databaseReference.push().getKey();
        Map<String, Object> flightData = new HashMap<>();
        flightData.put("id", flightId);
        flightData.put("airline", selectedAirline);
        flightData.put("flightNumber", flightNumber);
        flightData.put("origin", origin);
        flightData.put("destination", destination);
        flightData.put("departureTime", departureTime);
        flightData.put("arrivalTime", arrivalTime);
        flightData.put("price", price);

        if (flightId != null) {
            databaseReference.child(flightId).setValue(flightData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Flight details uploaded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
