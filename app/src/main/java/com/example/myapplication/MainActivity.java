package com.example.myapplication;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    //    ArrayList<TaskNode> taskList;
    public static final String CHANNEL_ID = "channel";
    Button btnAddNew;
    TextView title;
    RecyclerView taskView;
    ArrayList<TaskNode> taskList;
    DatabaseReference ref;
    TaskAdapter taskAdapter;


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


        title = findViewById(R.id.title);
        taskView = findViewById(R.id.tasks);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        taskView.setLayoutManager(manager);
        taskView.setHasFixedSize(true);
        taskList = new ArrayList<TaskNode>();
        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    TaskNode node = dataSnapshot1.getValue(TaskNode.class);
                    taskList.add(node);
                    Collections.sort(taskList); //сортировка ( taskNode имплементирует comparable )
                }
                taskAdapter = new TaskAdapter(MainActivity.this, taskList);
                taskView.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
