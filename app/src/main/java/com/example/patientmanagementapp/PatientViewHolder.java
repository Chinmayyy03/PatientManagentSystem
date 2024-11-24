package com.example.patientmanagementapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PatientViewHolder extends RecyclerView.Adapter<PatientViewHolder.ViewHolder> {

    private List<Patientinfo> patientLists;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(Patientinfo patientinfo, int position);
    }

    public PatientViewHolder(List<Patientinfo> patientLists, OnItemClickListener onItemClickListener) {
        this.patientLists = patientLists;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the RecyclerView item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_recycleview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the patient data at the current position
        Patientinfo patient = patientLists.get(position);

        // Bind the data to the ViewHolder
        holder.bind(patient);

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (onItemClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(patientLists.get(adapterPosition), adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientName, patientAge, disease, patientId;
        private ImageView patientImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views from the item layout
            patientName = itemView.findViewById(R.id.patientNametxt);
            patientAge = itemView.findViewById(R.id.patientAgetxt);
            disease = itemView.findViewById(R.id.diseasetxt);
            patientId = itemView.findViewById(R.id.patientId);
          // Ensure this ID matches your layout
        }

        public void bind(Patientinfo patientInfo) {
            patientName.setText(patientInfo.getP_Name());
            patientAge.setText("Age: " + patientInfo.getP_age());
            disease.setText("Disease: " + patientInfo.getP_Disease());
            patientId.setText("ID: " + patientInfo.getPatient_id());


        }
    }
}

