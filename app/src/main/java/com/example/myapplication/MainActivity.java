package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button btnAddNew;
    TextView title;
    RecyclerView taskView;
    ArrayList<TaskNode> taskList, taskList1, taskList2;
    DatabaseReference ref;
    TaskAdapter taskAdapter, taskAdapter1, taskAdapter2;
    Button btnTypeAll;

    Button btnTypeStudy;
    Button btnTypeWork;


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
        GridLayoutManager gridManager = new GridLayoutManager(this,2);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        taskView.setLayoutManager(layoutManager);
        taskView.setHasFixedSize(true);
        taskList = new ArrayList<TaskNode>();

        btnTypeAll = findViewById(R.id.btn_typeAll);
        btnTypeStudy = findViewById(R.id.btn_typeStudy);
        btnTypeWork = findViewById(R.id.btn_typeWork);

        taskList1 = new ArrayList<TaskNode>();
        taskList2 = new ArrayList<TaskNode>();

        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager");
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

                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getType().equalsIgnoreCase("Учёба"))
                        taskList1.add(taskList.get(i));
                    else if (taskList.get(i).getType().equalsIgnoreCase("Работа"))
                        taskList2.add(taskList.get(i));
                }

                taskAdapter1 = new TaskAdapter(MainActivity.this, taskList1);
                taskAdapter2 = new TaskAdapter(MainActivity.this, taskList2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        btnTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskView.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();
            }
        });

        btnTypeStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskView.setAdapter(taskAdapter1);
                taskAdapter1.notifyDataSetChanged();
            }
        });


        btnTypeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskView.setAdapter(taskAdapter2);
                taskAdapter2.notifyDataSetChanged();
            }
        });
    }
}