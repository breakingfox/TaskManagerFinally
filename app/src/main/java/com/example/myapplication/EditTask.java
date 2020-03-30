package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditTask extends AppCompatActivity {
    EditText title, description, date;
    Button btnSave, btnDelete;
    String key;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        date.setText(getIntent().getStringExtra("date"));
        key = getIntent().getStringExtra("key");
        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child("Task" + key);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("title").setValue(title.getText().toString());
                ref.child("description").setValue(description.getText().toString());
                ref.child("date").setValue(date.getText().toString());
                ref.child("key").setValue(Integer.parseInt(key));
                Intent edit = new Intent(EditTask.this, MainActivity.class);
                startActivity(edit);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( task.isSuccessful()) {
                            Intent deleted = new Intent(EditTask.this, MainActivity.class);
                            startActivity(deleted);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
