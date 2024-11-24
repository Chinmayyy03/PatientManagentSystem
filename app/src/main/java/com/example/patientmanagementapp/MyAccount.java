package com.example.patientmanagementapp;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.patientmanagementapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyAccount extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private ShapeableImageView profileImage;
    private EditText nameEditText,  genderEditText, cityEditText, stateEditText, nationalityEditText;
    private EditText emailEditText, phoneEditText, addressEditText;
    private Button dobEditText,saveButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        profileImage = findViewById(R.id.profileImage);
        nameEditText = findViewById(R.id.acc_name_edt);
        dobEditText = findViewById(R.id.acc_dob_edt);
        genderEditText = findViewById(R.id.acc_gender_edt);
        cityEditText = findViewById(R.id.acc_city_edt);
        stateEditText = findViewById(R.id.acc_state_edt);
        nationalityEditText = findViewById(R.id.acc_nationality_edt);
        emailEditText = findViewById(R.id.acc_email);
        phoneEditText = findViewById(R.id.acc_phone_edt);
        addressEditText = findViewById(R.id.acc_address);
        saveButton = findViewById(R.id.saveData);

        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userInfo");

        profileImage.setOnClickListener(v -> requestGalleryPermission());
        dobEditText.setOnClickListener(v -> showDatePickerDialog());

// Add this method


        saveButton.setOnClickListener(v -> saveUserData());

        loadUserData();
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date and set it as the button text
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dobEditText.setText(selectedDate);
                },
                year, month, day
        );

        // Show the dialog
        datePickerDialog.show();
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserData() {
        String name = nameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String nationality = nationalityEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Name and Email are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("firstName", name);
        userData.put("dob", dob);
        userData.put("gender", gender);
        userData.put("city", city);
        userData.put("state", state);
        userData.put("nationality", nationality);
        userData.put("email", email);
        userData.put("phoneNumber", phone);
        userData.put("address", address);

        if (selectedImageUri != null) {
            userData.put("profileImage", selectedImageUri.toString());
        }

        databaseReference.updateChildren(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MyAccount.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyAccount.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("firstName").getValue(String.class);
                    String dob = snapshot.child("dob").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String city = snapshot.child("city").getValue(String.class);
                    String state = snapshot.child("state").getValue(String.class);
                    String nationality = snapshot.child("nationality").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String imageUrl = snapshot.child("profileImage").getValue(String.class);

                    nameEditText.setText(name);
                    dobEditText.setText(dob);
                    genderEditText.setText(gender);
                    cityEditText.setText(city);
                    stateEditText.setText(state);
                    nationalityEditText.setText(nationality);
                    emailEditText.setText(email);
                    phoneEditText.setText(phone);
                    addressEditText.setText(address);

                    if (imageUrl != null) {
                        profileImage.setImageURI(Uri.parse(imageUrl));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyAccount.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
