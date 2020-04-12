package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTask extends AppCompatActivity {
    EditText title, description, date, etDatePicker, etTimePicker;
    Button btnSave, btnDelete;
    String key;
    DatabaseReference ref;
    Calendar calendar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        etDatePicker = findViewById(R.id.etDatePicker);
        etTimePicker = findViewById(R.id.etTimePicker);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        key = getIntent().getStringExtra("key");
        final String TAG = "EditTask";
        String time = getIntent().getStringExtra("calendar");
        Log.w(TAG, time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date tempDate = null;

        calendar = Calendar.getInstance();
        Log.w(TAG, String.valueOf(tempDate));
        try {
            calendar.setTime(dateFormat.parse(getIntent().getStringExtra("calendar")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final int curMonth = calendar.get(Calendar.MONTH) + 1;

        etTimePicker.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
        etDatePicker.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + curMonth + "." + calendar.get(Calendar.YEAR));
        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child("Task" + key);


        etDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTask.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        month++;
                        etDatePicker.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        etTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minutes);
                        etTimePicker.setText(hourOfDay + ":" + minutes);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("title").setValue(title.getText().toString());
                ref.child("description").setValue(description.getText().toString());
                ref.child("calendar").setValue(calendar.get(Calendar.DAY_OF_MONTH) + "." + curMonth + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
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
                        if (task.isSuccessful()) {
                            Intent deleted = new Intent(EditTask.this, MainActivity.class);
                            startActivity(deleted);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
