package com.example.patientmanagementapp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ChangeImage extends AppCompatActivity {

    Button b1,b2;
    ImageView iv;
    Uri imageuri;
    ProgressDialog pd;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        b1=(Button) findViewById(R.id.select);
        b2=(Button) findViewById(R.id.upload);
        iv=(ImageView) findViewById(R.id.imageView3);

        mAuth = FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }
    private void uploadImage() {
        pd = new ProgressDialog(this);
        pd.setTitle("Uploading File....");
        pd.show();

        if (imageuri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            pd.dismiss();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
            return;
        }

        String userId = user.getUid();
        // Convert Uri to String
        String imageUrl = imageuri.toString();

        // Create Database Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("userInfo");

        // Retrieve current UserInfo to preserve existing data
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Fetch existing user info
                UserInfo existingUserInfo = task.getResult().getValue(UserInfo.class);

                if (existingUserInfo != null) {
                    // Update the imgUrl only, preserving other fields
                    existingUserInfo.setImgUrl(imageUrl);

                    // Update user info in the database
                    databaseReference.setValue(existingUserInfo)
                            .addOnSuccessListener(aVoid -> {
                                pd.dismiss();
                                Toast.makeText(ChangeImage.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                                // Navigate to the account page
                                Intent intent = new Intent(ChangeImage.this, MyAccount.class);
                                intent.putExtra("uploadedImageUri", imageUrl);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                pd.dismiss();
                                Toast.makeText(ChangeImage.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                pd.dismiss();
                Toast.makeText(ChangeImage.this, "Failed to retrieve user info", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && data!=null && data.getData() !=null){
            imageuri=data.getData();
            iv.setImageURI(imageuri);
        }
    }
}