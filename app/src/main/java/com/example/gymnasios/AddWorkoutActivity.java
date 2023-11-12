package com.example.gymnasios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddWorkoutActivity extends AppCompatActivity {
    Button btnSave;
    EditText uploadFirstWeight, uploadSecondWeight, uploadThirdWeight, uploadFourthWeight,
            uploadFirstSet, uploadSecondSet, uploadThirdSet, uploadFourthSet, uploadExerciseName;
    TextView tvBackFromAddWorkout;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        auth = FirebaseAuth.getInstance();
        String currentUserUid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance("https://gymnasios-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("UserWorkouts").child(currentUserUid);

        uploadExerciseName = findViewById(R.id.uploadExcerciseName);
        uploadFirstSet = findViewById(R.id.uploadFirstSet);
        uploadSecondSet = findViewById(R.id.uploadSecondSet);
        uploadThirdSet = findViewById(R.id.uploadThirdSet);
        uploadFourthSet = findViewById(R.id.uploadFourthSet);
        uploadFirstWeight = findViewById(R.id.uploadFirstWeight);
        uploadSecondWeight = findViewById(R.id.uploadSecondWeight);
        uploadThirdWeight = findViewById(R.id.uploadThirdWeight);
        uploadFourthWeight = findViewById(R.id.uploadFourthWeight);
        tvBackFromAddWorkout = findViewById(R.id.tvBackFromAddWorkout);

        tvBackFromAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWorkoutActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExerciseNotEmpty()) {
                    if (areFieldsNotEmpty()) {
                        saveData(currentUserUid);
                    } else {
                        Toast.makeText(AddWorkoutActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddWorkoutActivity.this, "Please enter the exercise.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isExerciseNotEmpty() {
        return !TextUtils.isEmpty(uploadExerciseName.getText().toString());
    }

    private boolean areFieldsNotEmpty() {
        return !TextUtils.isEmpty(uploadFirstSet.getText().toString()) &&
                !TextUtils.isEmpty(uploadSecondSet.getText().toString()) &&
                !TextUtils.isEmpty(uploadThirdSet.getText().toString()) &&
                !TextUtils.isEmpty(uploadFourthSet.getText().toString()) &&
                !TextUtils.isEmpty(uploadFirstWeight.getText().toString()) &&
                !TextUtils.isEmpty(uploadSecondWeight.getText().toString()) &&
                !TextUtils.isEmpty(uploadThirdWeight.getText().toString()) &&
                !TextUtils.isEmpty(uploadFourthWeight.getText().toString());
    }

    private void saveData(String currentUserUid) {
        String exerciseName = uploadExerciseName.getText().toString();
        int firstSet = Integer.parseInt(uploadFirstSet.getText().toString().trim());
        int secondSet = Integer.parseInt(uploadSecondSet.getText().toString().trim());
        int thirdSet = Integer.parseInt(uploadThirdSet.getText().toString().trim());
        int fourthSet = Integer.parseInt(uploadFourthSet.getText().toString().trim());
        int firstWeight = Integer.parseInt(uploadFirstWeight.getText().toString().trim());
        int secondWeight = Integer.parseInt(uploadSecondWeight.getText().toString().trim());
        int thirdWeight = Integer.parseInt(uploadThirdWeight.getText().toString().trim());
        int fourthWeight = Integer.parseInt(uploadFourthWeight.getText().toString().trim());

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        DataClass dataClass = new DataClass(
                exerciseName,
                firstSet, secondSet, thirdSet, fourthSet,
                firstWeight, secondWeight, thirdWeight, fourthWeight,
                currentDate
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(AddWorkoutActivity.this);
        builder.setCancelable(false);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_layout, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        databaseReference.push().setValue(dataClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(AddWorkoutActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddWorkoutActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AddWorkoutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}