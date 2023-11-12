package com.example.gymnasios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitle, repetitionsEditText1, repetitionsEditText2, repetitionsEditText3, repetitionsEditText4,
            weightEditText2, weightEditText3, weightEditText4, weightEditText1, btnBackFromDetail;

    FloatingActionButton deleteButton, editButton;

    String key = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnBackFromDetail = findViewById(R.id.btnBackFromDetail);

        btnBackFromDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        detailTitle = findViewById(R.id.detailTitle);
        repetitionsEditText1 = findViewById(R.id.repetitionsEditText1);
        repetitionsEditText2 = findViewById(R.id.repetitionsEditText2);
        repetitionsEditText3 = findViewById(R.id.repetitionsEditText3);
        repetitionsEditText4 = findViewById(R.id.repetitionsEditText4);
        weightEditText1 = findViewById(R.id.weightEditText1);
        weightEditText2 = findViewById(R.id.weightEditText2);
        weightEditText3 = findViewById(R.id.weightEditText3);
        weightEditText4 = findViewById(R.id.weightEditText4);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailTitle.setText(bundle.getString("ExerciseName"));

            repetitionsEditText1.setText(String.valueOf(bundle.getInt("FirstSet")));
            weightEditText1.setText(String.valueOf(bundle.getInt("Weight1")));
            repetitionsEditText2.setText(String.valueOf(bundle.getInt("SecondSet")));
            weightEditText2.setText(String.valueOf(bundle.getInt("Weight2")));
            repetitionsEditText3.setText(String.valueOf(bundle.getInt("ThirdSet")));
            weightEditText3.setText(String.valueOf(bundle.getInt("Weight3")));
            repetitionsEditText4.setText(String.valueOf(bundle.getInt("FourthSet")));
            weightEditText4.setText(String.valueOf(bundle.getInt("Weight4")));
            key = bundle.getString("Key");
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataFromDatabaseAndStorage();
                navigateToHomeActivity();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void deleteDataFromDatabaseAndStorage() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://gymnasios-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("UserWorkouts")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(key);

        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                StorageReference storageReference = FirebaseStorage.getInstance("https://gymnasios-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference()
                        .child("Workout Set")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(key);

                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        navigateToHomeActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Failed to delete file from storage", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, "Failed to delete entry from database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToHomeActivity() {
        Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateData() {
        Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                .putExtra("Exercise", detailTitle.getText().toString())
                .putExtra("FirstSet", repetitionsEditText1.getText().toString())
                .putExtra("SecondSet", repetitionsEditText2.getText().toString())
                .putExtra("ThirdSet", repetitionsEditText3.getText().toString())
                .putExtra("FourthSet", repetitionsEditText4.getText().toString())
                .putExtra("Weight1", weightEditText1.getText().toString())
                .putExtra("Weight2", weightEditText2.getText().toString())
                .putExtra("Weight3", weightEditText3.getText().toString())
                .putExtra("Weight4", weightEditText4.getText().toString())
                .putExtra("Key", key);
        startActivity(intent);
    }
}