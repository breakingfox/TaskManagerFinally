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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class NewTask extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    TextView titles, addTitle, addDescription, addDate;
    Button btnSave, btnCancel;
    EditText title, description, etDatePicker, etTimePicker;
    DatabaseReference ref;
    Integer key = new Random().nextInt();

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        titles = findViewById(R.id.titles);
        addTitle = findViewById(R.id.addtTitle);
        title = findViewById(R.id.title);
        addDescription = findViewById(R.id.addDescription);
        description = findViewById(R.id.description);
        addDate = findViewById(R.id.addDate);
        //   date = findViewById(R.id.date);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        etDatePicker = findViewById(R.id.etDatePicker);
        etTimePicker = findViewById(R.id.etTimePicker);

        spinner = findViewById(R.id.spinner);

        final Calendar calendar = Calendar.getInstance();
        etDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(NewTask.this, 0, new DatePickerDialog.OnDateSetListener() {
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTask.this, new TimePickerDialog.OnTimeSetListener() {
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
                ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child("Task" + key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("title").setValue(title.getText().toString());
                        dataSnapshot.getRef().child("description").setValue(description.getText().toString());
                        dataSnapshot.getRef().child("key").setValue(key);

                        dataSnapshot.getRef().child("type").setValue(spinner.getSelectedItem().toString());

                        Log.w("NewTask", String.valueOf(calendar.get(Calendar.MONTH)));
                        dataSnapshot.getRef().child("calendar").setValue(calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                        Intent main = new Intent(NewTask.this, MainActivity.class);
                        startActivity(main);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
