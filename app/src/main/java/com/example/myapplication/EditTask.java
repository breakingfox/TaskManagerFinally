package com.example.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditTask extends AppCompatActivity {
    EditText title, description, date, etDatePicker, etTimePicker;
    Button btnSave, btnDelete;
    String key;
    DatabaseReference ref;
    Calendar calendar = null;
    Spinner spinner;
    ArrayList<String> types;
    public static final String SHARED_PREFS = "prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        spinner = findViewById(R.id.spinner2);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        etDatePicker = findViewById(R.id.etDatePicker);
        etTimePicker = findViewById(R.id.etTimePicker);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
        key = getIntent().getStringExtra("key");


        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String firstStart = preferences.getString("firstStart","0");

        types = new ArrayList<>();
        SharedPreferences.Editor editor = preferences.edit();
        int size = preferences.getInt("Types_size", 0);
        for(int i=0;i<size;i++)
        {
            types.add(preferences.getString("Types_" + i+1 , null));
        }


        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                types
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);

        String currentType = getIntent().getStringExtra("type");
        for (int i = 0; i < size; i++) {
            if(types.get(i).equalsIgnoreCase(currentType))
                spinner.setSelection(i);
        }


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

        String curHour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String curMinute = Integer.toString(calendar.get(Calendar.MINUTE));
        if (Integer.parseInt(curHour) < 10)
            curHour = "0" + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if (Integer.parseInt(curMinute) < 10)
            curMinute = "0" + Integer.toString(calendar.get(Calendar.MINUTE));
        etTimePicker.setText(curHour + ":" + curMinute);
        etDatePicker.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + curMonth + "." + calendar.get(Calendar.YEAR));
        ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child(firstStart).child("Task" + key);


//окна выбора даты и времени
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
                        String curHour = Integer.toString(hourOfDay);
                        String curMinute = Integer.toString(minutes);
                        if (hourOfDay < 10)
                            curHour = "0" + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                        if (minutes < 10)
                            curMinute = "0" + Integer.toString(calendar.get(Calendar.MINUTE));
                        etTimePicker.setText(curHour + ":" + curMinute);
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
                ref.child("type").setValue(spinner.getSelectedItem().toString());
                Intent edit = new Intent(EditTask.this, MainActivity.class);
                Intent notifyIntent = new Intent(EditTask.this, NotificationBroadcast.class);
                Log.w("EditTask", "Time im millis for notification: " + String.valueOf(calendar.getTimeInMillis() / 1000));

                Log.w("EditTask", "Title: " + title.getText().toString());
                Log.w("EditTask", "Desc: " + description.getText().toString());
                Log.w("EditTask", "ID: " + key);
                notifyIntent.putExtra("title", title.getText().toString());
                notifyIntent.putExtra("description", description.getText().toString());
                notifyIntent.putExtra("key", key);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(EditTask.this, Integer.parseInt(key), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar time = Calendar.getInstance();
                long temp = (calendar.getTimeInMillis() - time.getTimeInMillis()) / 1000;
                Log.w("EditTask", "Time now: " + String.valueOf(time.getTimeInMillis() / 1000));
                Log.w("EditTask", "Time diff: " + String.valueOf(temp));
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (calendar.after(Calendar.getInstance())) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-20000, pendingIntent);
                }
                else{
                    alarmManager.cancel(pendingIntent);
                }
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
                            Intent toMain = new Intent(EditTask.this, MainActivity.class);

                            Intent notifyIntent = new Intent(EditTask.this, NotificationBroadcast.class);
                            notifyIntent.putExtra("title", title.getText().toString());
                            notifyIntent.putExtra("description", description.getText().toString());
                            notifyIntent.putExtra("key", key);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(EditTask.this, Integer.parseInt(key), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                            startActivity(toMain);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
