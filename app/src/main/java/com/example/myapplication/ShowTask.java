package com.example.myapplication;

import android.os.Bundle;
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

public class ShowTask extends AppCompatActivity {
    //Неиспользуемый класс, который был необходим ранее
    TextView title;
    RecyclerView taskView;
    ArrayList<TaskNode> taskList;
    DatabaseReference ref;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
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
                taskAdapter = new TaskAdapter(ShowTask.this, taskList);
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
