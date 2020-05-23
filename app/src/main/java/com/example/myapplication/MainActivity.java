package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "channel";
    public static final String SHARED_PREFS = "prefs";
    Button btnAddNew;
    TextView title;
    RecyclerView taskView;
    ArrayList<TaskNode> taskList;
    DatabaseReference ref;
    TaskAdapter taskAdapter;

    Spinner spinner;
    ArrayList<String> taskTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAddNew = findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, NewTask.class);
                startActivity(a);
            }
        });



        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String firstStart = preferences.getString("firstStart","0");
        if(firstStart.equals("0")) {
            showId();
        }

        taskTypes = new ArrayList<String>();
        SharedPreferences.Editor editor = preferences.edit();
        int size = preferences.getInt("Types_size", 0);
        taskTypes.add("Все");
        for(int i=0;i<size;i++)
        {
            taskTypes.add(preferences.getString("Types_" + i+1 , null));
        }


        title = findViewById(R.id.title);
        taskView = findViewById(R.id.tasks);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 240);
        taskView.setLayoutManager(gridManager);
        taskView.setHasFixedSize(true);
        taskList = new ArrayList<TaskNode>();



        final ArrayList<ArrayList<TaskNode>> taskNodes = new ArrayList<>();
        for (int i = 0; i < taskTypes.size(); i++) {
            taskNodes.add(new ArrayList<TaskNode>());
        }


        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child(firstStart);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    TaskNode node = dataSnapshot1.getValue(TaskNode.class);
                    taskList.add(node);
                }

                Collections.sort(taskList); //сортировка ( taskNode имплементирует comparable )

                taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                taskView.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();

                for (int i = 1; i < taskTypes.size(); i++) { //taskTypes.size()
                    for (int j = 0; j < taskList.size(); j++) {
                        if(taskTypes.get(i).equalsIgnoreCase(taskList.get(j).getType()))
                            taskNodes.get(i).add(taskList.get(j));
                        if(i == 1) {
                            taskNodes.get(0).add(taskList.get(j));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
        final Animation fallingAnimation = AnimationUtils.loadAnimation(this, R.anim.falling_down);


        final ArrayList<TaskAdapter> taskAdapters = new ArrayList<>();
        for (int i = 0; i < taskNodes.size(); i++) {
            taskAdapters.add(new TaskAdapter(MainActivity.this,taskNodes.get(i)));
        }
        taskAdapters.add(new TaskAdapter(MainActivity.this,taskList));

        spinner = findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner1,
                taskTypes
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskView.setAdapter(taskAdapters.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showId()
    {
        String ID = Integer.toString((int)((Math.random()*100000000)));

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("firstStart",ID);
        editor.apply();
    }
}