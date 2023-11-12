package com.example.gymnasios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    Button updateButton;
    EditText updateFirstWeight, updateSecondWeight, updateThirdWeight, updateFourthWeight,
            updateFirstSet, updateSecondSet, updateThirdSet, updateFourthSet, updateExerciseName;

    String key;
    DatabaseReference databaseReference;

    TextView tvBackFromUpdateWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateFirstWeight = findViewById(R.id.updateFirstWeight);
        updateSecondWeight = findViewById(R.id.updateSecondWeight);
        updateThirdWeight = findViewById(R.id.updateThirdWeight);
        updateFourthWeight = findViewById(R.id.updateFourthWeight);
        updateFirstSet = findViewById(R.id.updateFirstSet);
        updateSecondSet = findViewById(R.id.updateSecondSet);
        updateThirdSet = findViewById(R.id.updateThirdSet);
        updateFourthSet = findViewById(R.id.updateFourthSet);
        updateExerciseName = findViewById(R.id.updateExcerciseName);
        tvBackFromUpdateWorkout = findViewById(R.id.tvBackFromUpdateWorkout);

        tvBackFromUpdateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            key = bundle.getString("Key");
            updateExerciseName.setText(bundle.getString("ExerciseName"));
            updateFirstWeight.setText(bundle.getString("Weight1"));
            updateSecondWeight.setText(bundle.getString("Weight2"));
            updateThirdWeight.setText(bundle.getString("Weight3"));
            updateFourthWeight.setText(bundle.getString("Weight4"));
            updateFirstSet.setText(bundle.getString("FirstSet"));
            updateSecondSet.setText(bundle.getString("SecondSet"));
            updateThirdSet.setText(bundle.getString("ThirdSet"));
            updateFourthSet.setText(bundle.getString("FourthSet"));
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserUid = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance("https://gymnasios-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("UserWorkouts")
                .child(currentUserUid)
                .child(key);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateData() {
        String exercise = updateExerciseName.getText().toString().trim();
        String firstSet = updateFirstSet.getText().toString().trim();
        String secondSet = updateSecondSet.getText().toString().trim();
        String thirdSet = updateThirdSet.getText().toString().trim();
        String fourthSet = updateFourthSet.getText().toString().trim();
        String firstWeight = updateFirstWeight.getText().toString().trim();
        String secondWeight = updateSecondWeight.getText().toString().trim();
        String thirdWeight = updateThirdWeight.getText().toString().trim();
        String fourthWeight = updateFourthWeight.getText().toString().trim();
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        DataClass dataClass = new DataClass(
                exercise,
                Integer.parseInt(firstSet),
                Integer.parseInt(secondSet),
                Integer.parseInt(thirdSet),
                Integer.parseInt(fourthSet),
                Integer.parseInt(firstWeight),
                Integer.parseInt(secondWeight),
                Integer.parseInt(thirdWeight),
                Integer.parseInt(fourthWeight),
                currentDate
        );

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Update Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}