package com.example.patientmanagementapp;

import static android.provider.MediaStore.Images.Media.insertImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientdetailsActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;

    private Button saveData;

    private EditText patientId, patientDate, patientName, patientntAge, patientDisease, patientDisease_info;
    private RadioButton male, female, other;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientdetails);

        // Firebase Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String username = currentUser.getDisplayName();
        if (username == null || username.isEmpty()) {
            username = currentUser.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("PatientInfo")
                .child("information")
                .child(username);

        // Initialize UI elements
//        patientImage = findViewById(R.id.patientImage);
        patientId = findViewById(R.id.pID_editText);
        patientDate = findViewById(R.id.date_edit);
        patientName = findViewById(R.id.patientName_editText);
        patientntAge = findViewById(R.id.Age_editText);
        patientDisease = findViewById(R.id.Disease_editText);
        patientDisease_info = findViewById(R.id.Description_editTextText);
        male = findViewById(R.id.Male_radioButton);
        female = findViewById(R.id.Female_radioButton);
        other = findViewById(R.id.Other_radioButton);
        saveData = findViewById(R.id.saveData);

        // Select Image


        // Set Current Date


        Intent intent = getIntent();
        String intentPatientId = intent.getStringExtra("PatientId");
        String intentPatientDate = intent.getStringExtra("PatientDate");
        String intentPatientName = intent.getStringExtra("PatientName");
        String intentPatientAge = intent.getStringExtra("PatientAge");
        String intentPatientGender = intent.getStringExtra("PatientGender");
        String intentPatientDisease = intent.getStringExtra("PatientDisease");
        String intentPatientDescription = intent.getStringExtra("PatientDescription");

        // Set the retrieved data to the respective fields
        patientId.setText(intentPatientId);
        patientDate.setText(intentPatientDate);
        patientName.setText(intentPatientName);
        patientntAge.setText(intentPatientAge);
        if (intentPatientGender != null) {
            switch (intentPatientGender.toLowerCase()) {
                case "male":
                    male.setChecked(true);
                    break;
                case "female":
                    female.setChecked(true);
                    break;
                case "other":
                    other.setChecked(true);
                    break;
                default:
                    // Handle invalid or no gender (optional)
                    break;
            }
        }
        patientDisease.setText(intentPatientDisease);
        patientDisease_info.setText(intentPatientDescription);



        if (intentPatientDate != null && !intentPatientDate.isEmpty()) {
            // Use the date from the Intent
            patientDate.setText(intentPatientDate);
        } else {
            // Set the current date if no date is passed in the Intent
            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            patientDate.setText(currentDate);
        }
        patientDate.setEnabled(false);
        // Save Data
        saveData.setOnClickListener(v -> savePatientDetails());
    }







    // Update the savePatientDetails method
    private void savePatientDetails() {
        String pId = patientId.getText().toString();
        String pDate = patientDate.getText().toString();
        String pName = patientName.getText().toString();
        String pAge = patientntAge.getText().toString();
        String pDisease = patientDisease.getText().toString();
        String pDiseaseInfo = patientDisease_info.getText().toString();
        String gender = male.isChecked() ? "Male" : female.isChecked() ? "Female" : "Other";

        if (pId.isEmpty() || pDate.isEmpty() || pName.isEmpty() || pAge.isEmpty() || pDisease.isEmpty() || pDiseaseInfo.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }




        saveToDatabase(pId, pDate, pName, pAge, gender, pDisease, pDiseaseInfo);
    }



    private void saveToDatabase(String pId, String pDate, String pName, String pAge, String gender, String pDisease, String pDiseaseInfo) {
        Patientinfo patientInfo = new Patientinfo(pId, pDate, pName, pAge, gender, pDisease, pDiseaseInfo);
        databaseReference.child(pName).setValue(patientInfo).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Patient details saved successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to save patient details!", Toast.LENGTH_SHORT).show();
        });
    }

    // Retrieve image as Bitmap from List<Integer>
//    private Bitmap decodeImageFromList(List<Integer> imageBytesList) {
//        if (imageBytesList == null) return null;
//        byte[] bytes = new byte[imageBytesList.size()];
//        for (int i = 0; i < imageBytesList.size(); i++) {
//            bytes[i] = imageBytesList.get(i).byteValue(); // Convert Integer back to byte
//        }
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//    }

    private void clearFields() {
        patientId.setText("");
        patientDate.setText("Select Date");
        patientName.setText("");
        patientntAge.setText("");
        patientDisease.setText("");
        patientDisease_info.setText("");
        male.setChecked(false);
        female.setChecked(false);
        other.setChecked(false);

    }
}
