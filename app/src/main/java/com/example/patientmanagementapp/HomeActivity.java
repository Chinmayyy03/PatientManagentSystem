package com.example.patientmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;
    private List<Patientinfo> patientLists;

    private PatientViewHolder adapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientLists = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        // Load data from SQLite
//        loadDataFromSQLite();

        // Retrieve data from Firebase
        loadDataFromFirebase();

        // Set up the RecyclerView adapter
        adapter = new PatientViewHolder(patientLists, new PatientViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(Patientinfo patientinfo, int position) {
                Intent intent = new Intent(HomeActivity.this, PatientdetailsActivity.class);
                intent.putExtra("PatientId", patientinfo.getPatient_id());
                intent.putExtra("PatientDate",patientinfo.getDate());
                intent.putExtra("PatientName", patientinfo.getP_Name());
                intent.putExtra("PatientGender",patientinfo.getP_gender());
                intent.putExtra("PatientAge", patientinfo.getP_age());
                intent.putExtra("PatientDisease", patientinfo.getP_Disease());
                intent.putExtra("PatientDescription", patientinfo.getP_Diseasedescription());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        // Ensure the user is logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    // Method to load data from Firebase
    private void loadDataFromFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        // Get the UID of the current user
        String userUid = currentUser != null ? currentUser.getUid() : null;

//        if (userUid == null) {
//            Toast.makeText(HomeActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Reference to the specific user's data in Firebase
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("PatientInfo")
                .child("information")
                .child(userUid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing list before adding fresh data
                patientLists.clear();

                // First loop: Iterate through each patient name dynamically
//                for (DataSnapshot patientNameSnapshot : dataSnapshot.getChildren()) {
                    // Get the patient name (e.g., "Radhey Patil", "rrfc", etc.)


                    // Second loop: Iterate through patient data under the patient name
                    for (DataSnapshot patientDataSnapshot : dataSnapshot.getChildren()) {

                        String patientName = patientDataSnapshot.getKey();
                        String patientId = patientDataSnapshot.child("patient_id").getValue(String.class);
                        String patientAge = patientDataSnapshot.child("p_age").getValue(String.class);
                        String gender = patientDataSnapshot.child("p_gender").getValue(String.class);
                        String patientDisease = patientDataSnapshot.child("p_Disease").getValue(String.class);
                        String patientDiseaseDescription = patientDataSnapshot.child("p_Diseasedescription").getValue(String.class);
                        String date = patientDataSnapshot.child("date").getValue(String.class);

                        // Ensure all data is available
                        if (patientName != null && patientId != null && patientAge != null && patientDisease != null) {
                            // Create a new Patientinfo object
                            Patientinfo patientInfo = new Patientinfo();
                            patientInfo.setPatient_id(patientId);
                            patientInfo.setP_Name(patientName);
                            patientInfo.setP_age(patientAge);
                            patientInfo.setP_gender(gender);
                            patientInfo.setP_Disease(patientDisease);
                            patientInfo.setP_Diseasedescription(patientDiseaseDescription);
                            patientInfo.setDate(date);


                            // Add the object to the list
                            patientLists.add(patientInfo);
//                        }
                    }
                }
                // Notify adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error loading Firebase data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            startActivity(new Intent(HomeActivity.this, MyAccount.class));
            return true;
        } else if (id == R.id.add) {
            startActivity(new Intent(HomeActivity.this, PatientdetailsActivity.class));
            return true;
        } else if (id == R.id.home) {
            return true;
        } else if (id == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(HomeActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return false;
    }
}
