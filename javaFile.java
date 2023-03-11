package com.example.maintenanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ReportsActivity extends AppCompatActivity {

    Object da;
    String unit;
    ImageView drop;
    String sort = "A" ;
    Date d = new Date();
    RadioGroup radioGroup;
    tasks_adapter mAdapter;
    boolean  isDown = false;
    String year, month, week;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> tasks = new ArrayList<>();
    ArrayList<Long> problems = new ArrayList<>();
    ArrayList<tasks_item> items = new ArrayList<>();
    ArrayList<String> departments = new ArrayList<>();
    ArrayList<RadioButton> radioButtons = new ArrayList<>();
    SimpleDateFormat dateFormat, thisYear, thisMonth, thisWeek;
    String building , first, second, user, date;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5,
            radioButton6, radioButton7, radioButton9;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        building = DataHandler.getBuilding();
        mRecyclerView = findViewById(R.id.recyclerView_R);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new tasks_adapter(items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setAdapter(mAdapter);
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);
        radioButton6 = findViewById(R.id.radioButton6);
        radioButton7 = findViewById(R.id.radioButton7);
        radioButton9 = findViewById(R.id.radioButton9);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1.setChecked(true);
        radioButton9.setChecked(true);
        drop = findViewById(R.id.imageView3);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        thisYear = new SimpleDateFormat("yyyy");
        thisMonth = new SimpleDateFormat("M");
        thisWeek = new SimpleDateFormat("w");
        year = String.valueOf(Integer.parseInt(thisYear.format(d)));
        month = String.valueOf(Integer.parseInt(thisMonth.format(d)));
        week = String.valueOf(Integer.parseInt(thisWeek.format(d)));
        drop.setOnClickListener(v -> {
            if (!isDown) {
                drop.setRotation(90);
                radioButton1.setVisibility(View.VISIBLE);
                radioButton2.setVisibility(View.VISIBLE);
                radioButton3.setVisibility(View.VISIBLE);
                radioButton4.setVisibility(View.VISIBLE);
                radioButton5.setVisibility(View.VISIBLE);
                radioButton6.setVisibility(View.VISIBLE);
                radioButton7.setVisibility(View.VISIBLE);
                for (int i = 0; i < radioButtons.size(); i++) {
                    radioButtons.get(i).setVisibility(View.VISIBLE);
                }
                isDown = true;
            }else {
                radioButton2.setVisibility(View.GONE);
                radioButton3.setVisibility(View.GONE);
                radioButton4.setVisibility(View.GONE);
                radioButton5.setVisibility(View.GONE);
                radioButton6.setVisibility(View.GONE);
                radioButton7.setVisibility(View.GONE);
                drop.setRotation(-90);
                if (Objects.equals(sort, "A")){
                    radioButton1.setVisibility(View.VISIBLE);
                    radioButton1.setChecked(true);
                }else if (Objects.equals(sort, "TY")) {
                    radioButton2.setVisibility(View.VISIBLE);
                    radioButton2.setChecked(true);
                }else if (Objects.equals(sort, "LY")) {
                    radioButton3.setVisibility(View.VISIBLE);
                    radioButton3.setChecked(true);
                }else if (Objects.equals(sort, "TM")) {
                    radioButton4.setVisibility(View.VISIBLE);
                    radioButton4.setChecked(true);
                }else if (Objects.equals(sort, "LM")) {
                    radioButton5.setVisibility(View.VISIBLE);
                    radioButton5.setChecked(true);
                }else if (Objects.equals(sort, "TW")) {
                    radioButton6.setVisibility(View.VISIBLE);
                    radioButton6.setChecked(true);
                }else if (Objects.equals(sort, "LW")) {
                    radioButton7.setVisibility(View.VISIBLE);
                    radioButton7.setChecked(true);
                }
                isDown = false;
            }
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

        });
        mAdapter.setOnItemClickListener(new tasks_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent myIntent = new Intent(ReportsActivity.this, CheckTasksActivity.class);
                DataHandler.setReport(tasks.get(position));
                DataHandler.setProblems(Math.toIntExact(problems.get(position)));
                startActivity(myIntent);
            }
            @Override
            public void onEditClick(int position) {}
        });
        fillDepartments();
        getData();
    }
    public void fillDepartments(){
        DataHandler.clearDepartments();
        DataHandler.clearDepartmentsNames();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery = database.child("Buildings").child(building)
                .child("departments");
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    DataHandler.setDepartments(noteSnapshot.getKey());
                    DataHandler.setDepartmentsNames((String) noteSnapshot.child("name").getValue());
                }
                for (int i = 0; i < DataHandler.departmentsNames.size(); i++) {
                    String item = DataHandler.getDepartmentsNames(i);

                    ColorStateList colorStateList = ColorStateList.valueOf(Color.WHITE);
                    RadioButton radioButton = new RadioButton(ReportsActivity.this);
                    radioButton.setText(item);
                    radioButton.setId(i);
                    radioButton.setTextColor(Color.WHITE);
                    radioButton.setButtonTintList(colorStateList);
                    radioButton.setVisibility(View.GONE);
                    radioButtons.add(radioButton);
                    radioGroup.addView(radioButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG", error.toString());
            }
        });

    }
    public void getData(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query recentPostsQuery = database.child("Buildings").child(building)
                .child("Reports");
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                tasks.clear();
                departments.clear();
                problems.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    if (noteSnapshot.child("date").exists()) {
                        tasks.add(noteSnapshot.getKey());
                        first = (String) noteSnapshot.child("department").getValue();
                        departments.add(first);
                        second = noteSnapshot.child("comments").getValue() + " Problems";
                        problems.add((Long) noteSnapshot.child("comments").getValue());
                        user = (String) noteSnapshot.child("user").getValue();
                        Log.i("TAG", noteSnapshot.child("date").getValue() + "");
                        date = dateFormat.format(noteSnapshot.child("date").getValue());
                        da = noteSnapshot.child("date").getValue();
                        switch (sort) {
                            case "A":
                                items.add(new tasks_item(first, second, user, date, null, false));
                                break;
                            case "TY":
                                if (Objects.equals(thisYear.format(da), year)) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                            case "LY":
                                if (thisYear.format(da).equals(String.valueOf(Integer.parseInt(year)-1))) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                            case "TM":
                                if (thisMonth.format(da).equals(month)) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                            case "LM":
                                if (thisMonth.format(da).equals(String.valueOf(Integer.parseInt(month)-1))) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                            case "TW":
                                if (thisWeek.format(da).equals(week)) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                            case "LW":
                                if (thisWeek.format(da).equals(String.valueOf(Integer.parseInt(week)-1))) {
                                    items.add(new tasks_item(first, second, user, date, null, false));
                                }
                                break;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG", error.toString());
            }
        });
    }
    public void click(View view){
        radioButton1.setVisibility(View.GONE);
        radioButton2.setVisibility(View.GONE);
        radioButton3.setVisibility(View.GONE);
        radioButton4.setVisibility(View.GONE);
        radioButton5.setVisibility(View.GONE);
        radioButton6.setVisibility(View.GONE);
        radioButton7.setVisibility(View.GONE);
        drop.setRotation(-90);
        isDown = false;
        if (view.getId() == R.id.radioButton){
            sort = "A";
            radioButton1.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton2){
            sort = "TY";
            radioButton2.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton3){
            sort = "LY";
            radioButton3.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton4){
            sort = "TM";
            radioButton4.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton5){
            sort = "LM";
            radioButton5.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton6){
            sort = "TW";
            radioButton6.setVisibility(View.VISIBLE);
        }else if (view.getId() == R.id.radioButton7){
            sort = "LW";
            radioButton7.setVisibility(View.VISIBLE);
        }
        getData();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
