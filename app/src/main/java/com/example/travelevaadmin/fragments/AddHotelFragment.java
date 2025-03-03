package com.example.travelevaadmin.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.travelevaadmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddHotelFragment extends Fragment {

    private EditText editTextHotelName, editTextCity, editTextAddress, editTextPrice;
    private CheckBox checkBoxRoomService, checkBoxFreeParking, checkBoxFreeWiFi, checkBoxFamilyRooms,
            checkBox24HourFrontDesk, checkBoxAirConditioning, checkBoxLaundry, checkBoxDailyHousekeeping, checkBoxBreakfast;
    private Button buttonSave, buttonUploadImage1, buttonUploadImage2, buttonUploadImage3;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String hotelId;
    private List<Uri> selectedImages = new ArrayList<>();

    public AddHotelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("hotels");
        storageReference = FirebaseStorage.getInstance().getReference("hotel_images");

        editTextHotelName = view.findViewById(R.id.editTextHotelName);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextPrice = view.findViewById(R.id.editTextPrice);

        checkBoxRoomService = view.findViewById(R.id.checkBoxRoomService);
        checkBoxFreeParking = view.findViewById(R.id.checkBoxFreeParking);
        checkBoxFreeWiFi = view.findViewById(R.id.checkBoxFreeWiFi);
        checkBoxFamilyRooms = view.findViewById(R.id.checkBoxFamilyRooms);
        checkBox24HourFrontDesk = view.findViewById(R.id.checkBox24HourFrontDesk);
        checkBoxAirConditioning = view.findViewById(R.id.checkBoxAirConditioning);
        checkBoxLaundry = view.findViewById(R.id.checkBoxLaundry);
        checkBoxDailyHousekeeping = view.findViewById(R.id.checkBoxDailyHousekeeping);
        checkBoxBreakfast = view.findViewById(R.id.checkBoxBreakfast);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonUploadImage1 = view.findViewById(R.id.buttonUploadImage1);
        buttonUploadImage2 = view.findViewById(R.id.buttonUploadImage2);
        buttonUploadImage3 = view.findViewById(R.id.buttonUploadImage3);

        buttonSave.setOnClickListener(v -> saveHotelData());

        buttonUploadImage1.setOnClickListener(v -> selectImage());
        buttonUploadImage2.setOnClickListener(v -> selectImage());
        buttonUploadImage3.setOnClickListener(v -> selectImage());
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    selectedImages.add(imageUri);
                    Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
                }
            });

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void saveHotelData() {
        String hotelName = editTextHotelName.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        if (hotelName.isEmpty() || city.isEmpty() || address.isEmpty() || price.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        hotelId = databaseReference.push().getKey();

        Map<String, Object> hotelData = new HashMap<>();
        hotelData.put("hotelName", hotelName);
        hotelData.put("city", city);
        hotelData.put("address", address);
        hotelData.put("price", price);
        hotelData.put("Room Service", checkBoxRoomService.isChecked());
        hotelData.put("Free Parking", checkBoxFreeParking.isChecked());
        hotelData.put("Free WiFi", checkBoxFreeWiFi.isChecked());
        hotelData.put("Family Rooms", checkBoxFamilyRooms.isChecked());
        hotelData.put("24-Hour Front Desk", checkBox24HourFrontDesk.isChecked());
        hotelData.put("Air Conditioning", checkBoxAirConditioning.isChecked());
        hotelData.put("Laundry", checkBoxLaundry.isChecked());
        hotelData.put("Daily Housekeeping", checkBoxDailyHousekeeping.isChecked());
        hotelData.put("Breakfast", checkBoxBreakfast.isChecked());

        if (hotelId != null) {
            databaseReference.child(hotelId).setValue(hotelData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Hotel saved. Uploading images...", Toast.LENGTH_SHORT).show();
                        uploadAllImages();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving hotel", Toast.LENGTH_SHORT).show());
        }
    }

    private void uploadAllImages() {
        if (hotelId == null || selectedImages.isEmpty()) {
            Toast.makeText(getContext(), "No images selected", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < selectedImages.size(); i++) {
            Uri imageUri = selectedImages.get(i);
            uploadImageToFirebase(imageUri, i + 1);
        }
    }

    private void uploadImageToFirebase(Uri imageUri, int imageNumber) {
        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageReference.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    databaseReference.child(hotelId).child("image" + imageNumber).setValue(imageUrl)
                            .addOnSuccessListener(aVoid -> {
                                if (imageNumber == selectedImages.size()) {
                                    Toast.makeText(getContext(), "All images uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show());
    }
}
