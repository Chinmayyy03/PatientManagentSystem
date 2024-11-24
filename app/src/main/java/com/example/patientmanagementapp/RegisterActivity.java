package com.example.patientmanagementapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password, confirmpassword, firstName, lastName, mobileNo;
    TextView loginpage;
    Button register1;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        firstName = findViewById(R.id.RegFirstName);
        lastName = findViewById(R.id.RegLastName);
        mobileNo = findViewById(R.id.RegPhoneNo);
        register1 = findViewById(R.id.registerbtn);

        register1.setOnClickListener(v -> {
            String emailid = email.getText().toString();
            String pass = password.getText().toString();
            String confirmpass = confirmpassword.getText().toString();
            String fname = firstName.getText().toString();
            String lname = lastName.getText().toString();
            // Get the phone number from the EditText
            String mobno = mobileNo.getText().toString();


            if (fname.isEmpty()) {
                firstName.setError("Please enter first name");
                firstName.requestFocus();
            } else if (lname.isEmpty()) {
                lastName.setError("Please enter last name");
                lastName.requestFocus();
            } else if (mobno.isEmpty()) {
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
                return;
                // Check if the phone number is exactly 10 digits long
            }else if (mobno.length() != 10) {
                mobileNo.setError("Phone number must be 10 digits long");
                mobileNo.requestFocus();
                return; // Return from the method without proceeding further
            } else if (emailid.isEmpty() && pass.isEmpty() && confirmpass.isEmpty() && fname.isEmpty() && lname.isEmpty() && mobno.isEmpty()) {
                email.setError("Please enter email id");
                email.requestFocus();
                password.setError("Please enter password");
                password.requestFocus();
                confirmpassword.setError("Please enter confirm password");
                confirmpassword.requestFocus();
                firstName.setError("Please enter first name");
                firstName.requestFocus();
                lastName.setError("Please enter last name");
                lastName.requestFocus();
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
            } else if (!pass.equals(confirmpass)) {
                Toast.makeText(RegisterActivity.this, "Password and Confirm Password are not same", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(RegisterActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user = task.getResult().getUser();
                        // Send email verification
                        user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                            if (emailVerificationTask.isSuccessful()) {

                                // Email sent successfully, you can navigate to the main activity or show a confirmation message
                                // FirebaseUser user = task.getResult().getUser();
                                saveAdditionalUserInfo(user.getUid(),fname, lname, mobno,emailid);

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                Toast.makeText(this, "registered successfully,now you can login with the credentials", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                // Email verification failed, handle the error
                                Toast.makeText(RegisterActivity.this, "Email verification failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        loginpage = findViewById(R.id.loginbtnreg);
        loginpage.setOnClickListener(view -> {
            Intent stm = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(stm);
            finish();
        });

    }

    private void saveAdditionalUserInfo(String userId, String firstName, String lastName, String phoneNumber, String email) {
        // Initialize Firebase database reference
       // Create UserInfo object
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("userInfo");
        UserInfo userInfo = new UserInfo(email, firstName, lastName, phoneNumber, userId);
        // Set user info in the database
        databaseReference.setValue(userInfo);
        Toast.makeText(this, "user info saved successfully", Toast.LENGTH_SHORT).show();
    }


}