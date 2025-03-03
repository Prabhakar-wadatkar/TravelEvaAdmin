package com.example.travelevaadmin.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.travelevaadmin.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddPlaceFragment extends Fragment {

    private TextInputEditText editTextTitle, editTextDescription, editTextCharges, editTextParkingAvailable, editTextAddress, editTextFeatures, editTextSpecialRules, editTextCity;
    private CheckBox checkBoxHillStation, checkBoxBeach, checkBoxHistorical, checkBoxForts, checkBoxWaterfalls, checkBoxReligiousPlaces;
    private RadioGroup radioGroupAddedBy;
    private ImageView imageViewUploaded;
    private MaterialButton buttonUpload;
    private TextView buttonUploadImage;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private Uri imageUri;
    private static final int IMAGE_PICK_CODE = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("top_places");

        // Initialize UI components
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextCharges = view.findViewById(R.id.editTextCharges);
        editTextParkingAvailable = view.findViewById(R.id.editTextParkingAvailable);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextFeatures = view.findViewById(R.id.editTextFeatures);
        editTextSpecialRules = view.findViewById(R.id.editTextSpecialRules);
        editTextCity = view.findViewById(R.id.editTextCity);

        checkBoxHillStation = view.findViewById(R.id.checkBoxHillStation);
        checkBoxBeach = view.findViewById(R.id.checkBoxBeach);
        checkBoxHistorical = view.findViewById(R.id.checkBoxHistorical);
        checkBoxForts = view.findViewById(R.id.checkBoxForts);
        checkBoxWaterfalls = view.findViewById(R.id.checkBoxWaterfalls);
        checkBoxReligiousPlaces = view.findViewById(R.id.checkBoxReligiousPlaces);

        radioGroupAddedBy = view.findViewById(R.id.radioGroupAddedBy);
        imageViewUploaded = view.findViewById(R.id.imageViewUploaded);
        buttonUploadImage = view.findViewById(R.id.buttonUploadImage);
        buttonUpload = view.findViewById(R.id.buttonUpload);

        progressBar = view.findViewById(R.id.progressBar);

        // Set up upload button listener
        buttonUpload.setOnClickListener(v -> uploadDataToFirebase());

        // Set up image upload button listener
        buttonUploadImage.setOnClickListener(v -> selectImage());

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Select image files only
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewUploaded.setImageURI(imageUri); // Display selected image
        }
    }

    private void uploadDataToFirebase() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String charges = editTextCharges.getText().toString().trim();
        String parkingAvailable = editTextParkingAvailable.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String features = editTextFeatures.getText().toString().trim();
        String specialRules = editTextSpecialRules.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();

        // Get selected categories
        StringBuilder categories = new StringBuilder();
        if (checkBoxHillStation.isChecked()) categories.append("Hill Station, ");
        if (checkBoxBeach.isChecked()) categories.append("Beach, ");
        if (checkBoxHistorical.isChecked()) categories.append("Historical Site, ");
        if (checkBoxForts.isChecked()) categories.append("Forts, ");
        if (checkBoxWaterfalls.isChecked()) categories.append("Waterfalls, ");
        if (checkBoxReligiousPlaces.isChecked()) categories.append("Religious Places, ");

        if (categories.length() > 0) {
            categories.setLength(categories.length() - 2); // Remove trailing comma and space
        }

        // Get the selected radio button for "Added By"
        int selectedAddedById = radioGroupAddedBy.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = radioGroupAddedBy.findViewById(selectedAddedById);
        String addedBy = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        // Validate required fields
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(charges) || TextUtils.isEmpty(parkingAvailable) || TextUtils.isEmpty(address) || TextUtils.isEmpty(features) || TextUtils.isEmpty(specialRules) || TextUtils.isEmpty(city) || TextUtils.isEmpty(addedBy)) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add current date and time
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Prepare data to upload
        Map<String, Object> placeData = new HashMap<>();
        placeData.put("title", title);
        placeData.put("description", description);
        placeData.put("charges", charges);
        placeData.put("parkingAvailable", parkingAvailable);
        placeData.put("address", address);
        placeData.put("features", features);
        placeData.put("specialRules", specialRules);
        placeData.put("city", city);
        placeData.put("categories", categories.toString());
        placeData.put("addedBy", addedBy);
        placeData.put("timestamp", currentDate);

        // Show the progress bar while uploading
        progressBar.setVisibility(View.VISIBLE);

        // If image is selected, upload it to Firebase Storage
        if (imageUri != null) {
            uploadImageToFirebaseStorage(imageUri, placeData);
        } else {
            // If no image is selected, upload data without the image URL
            databaseReference.push().setValue(placeData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Data uploaded successfully!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Hide the progress bar
                        clearForm();  // Clear the form
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE); // Hide the progress bar
                    });
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, Map<String, Object> placeData) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique file name
        String fileName = UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child("Places_images/" + fileName);

        // Upload the image
        fileRef.putFile(imageUri)
                .addOnProgressListener(taskSnapshot -> {
                    // Update progress bar during upload
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                })
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Get the download URL of the uploaded image
                    String imageUrl = uri.toString();

                    // Add image URL to the data
                    placeData.put("imageUrl", imageUrl);

                    // Upload the data to Firebase Database
                    databaseReference.push().setValue(placeData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Data uploaded successfully!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE); // Hide the progress bar
                                clearForm();  // Clear the form
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE); // Hide the progress bar
                            });
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // Hide the progress bar
                });
    }

    private void clearForm() {
        // Clear text fields
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextCharges.setText("");
        editTextParkingAvailable.setText("");
        editTextAddress.setText("");
        editTextFeatures.setText("");
        editTextSpecialRules.setText("");
        editTextCity.setText("");

        // Clear image view
        imageViewUploaded.setImageResource(R.drawable.ic_upload);  // Reset the uploaded image placeholder
        imageUri = null;  // Clear the selected image URI

        // Uncheck all checkboxes
        checkBoxHillStation.setChecked(false);
        checkBoxBeach.setChecked(false);
        checkBoxHistorical.setChecked(false);
        checkBoxForts.setChecked(false);
        checkBoxWaterfalls.setChecked(false);
        checkBoxReligiousPlaces.setChecked(false);

        // Reset radio group (deselect radio button)
        radioGroupAddedBy.clearCheck();  // This will deselect any selected radio button
    }
}
