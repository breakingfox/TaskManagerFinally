package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    RecyclerView taskView, typeView;
    ArrayList<TaskNode> taskList, taskList1, taskList2;

    ArrayList<String> taskTypes;
    TypeAdapter typeAdapter;

    DatabaseReference ref;
    TaskAdapter taskAdapter, taskAdapter1, taskAdapter2;
    Button btnTypeAll;

    Button btnTypeStudy;
    Button btnTypeWork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String firstStart = preferences.getString("firstStart","0");
        if(firstStart.equals("0")) {
            showId();
        }

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

        typeView = findViewById(R.id.task_types);
        LinearLayoutManager manager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        typeView.setLayoutManager(manager1);
        typeView.setHasFixedSize(true);
        taskTypes = new ArrayList<>();
        typeAdapter = new TypeAdapter(taskTypes);



        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        taskView.setLayoutManager(gridManager);
        taskView.setHasFixedSize(true);
        taskList = new ArrayList<TaskNode>();

        btnTypeAll = findViewById(R.id.btn_typeAll);
        btnTypeStudy = findViewById(R.id.btn_typeStudy);
        btnTypeWork = findViewById(R.id.btn_typeWork);

        taskList1 = new ArrayList<TaskNode>();
        taskList2 = new ArrayList<TaskNode>();

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
        final Animation fallingAnimation = AnimationUtils.loadAnimation(this, R.anim.falling_down);
        btnTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskView.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();
                btnTypeAll.startAnimation(fallingAnimation);


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

    private void showId()
    {
        String ID = Integer.toString((int)((Math.random()*100000000)));

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("firstStart",ID);
        editor.apply();
    }
}
