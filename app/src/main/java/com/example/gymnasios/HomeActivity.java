package com.example.gymnasios;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gymnasios.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



public class HomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataClassList;
    DatabaseReference databaseReference;
    MyAdapterRecycler adapter;
    FirebaseAuth auth;
    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    ValueEventListener eventListener;
    SearchView searchView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fab = findViewById(R.id.btnAddWorkout);
        recyclerView = findViewById(R.id.recyclerViewWorkout);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();
        dialog.show();

        dataClassList = new ArrayList<>();
        adapter = new MyAdapterRecycler(this, dataClassList);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        String currentUserUid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance("https://gymnasios-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("UserWorkouts").child(currentUserUid);

        Query originalQuery = databaseReference.orderByChild("date");

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataClassList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataClassList.add(dataClass);
                }


                Collections.sort(dataClassList, new Comparator<DataClass>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");

                    @Override
                    public int compare(DataClass data1, DataClass data2) {
                        try {
                            Date date1 = dateFormat.parse(data1.getDate());
                            Date date2 = dateFormat.parse(data2.getDate());
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        };

        originalQuery.addValueEventListener(eventListener);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddWorkoutActivity.class);
                startActivity(intent);
                finish();
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.nav_home_screen: {
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.nav_street_workout: {
                        Intent intent = new Intent(HomeActivity.this, StreetWorkoutActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.nav_logout: {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.nav_walking: {
                        Intent intent = new Intent(HomeActivity.this, WalkingActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.nav_stopwatch: {
                        Intent intent = new Intent(HomeActivity.this, StopwatchActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryDatabaseForSearch(newText);
                return true;
            }
        });
    }

    private void queryDatabaseForSearch(String searchText) {
        Query searchQuery = databaseReference.orderByChild("exercise")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataClassList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataClassList.add(dataClass);
                }


                Collections.sort(dataClassList, new Comparator<DataClass>() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");

                    @Override
                    public int compare(DataClass data1, DataClass data2) {
                        try {
                            Date date1 = dateFormat.parse(data1.getDate());
                            Date date2 = dateFormat.parse(data2.getDate());
                            return date2.compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}