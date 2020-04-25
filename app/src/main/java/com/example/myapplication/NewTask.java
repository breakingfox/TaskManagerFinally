package com.example.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class NewTask extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    public static final String CHANNEL_ID_1 = "channel1";
    TextView titles, addTitle, addDescription, addDate;
    Button btnSave, btnCancel;
    EditText title, description, etDatePicker, etTimePicker;
    DatabaseReference ref;
    Integer key = new Random().nextInt();
    NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat notificationManager;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_old);
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


        //==============================================================================

        createNotificationChannel();

        notificationManager = NotificationManagerCompat.from(this);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewTask.this, "Been here, done that", Toast.LENGTH_SHORT).show();
                ref = FirebaseDatabase.getInstance().getReference().child("TaskManager").child("Task" + key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("title").setValue(title.getText().toString());
                        dataSnapshot.getRef().child("description").setValue(description.getText().toString());
                        dataSnapshot.getRef().child("key").setValue(key);
                        //   Log.w("NewTask", String.valueOf(calendar.get(Calendar.MONTH)));
                        int curMonth = calendar.get(Calendar.MONTH) + 1;
                        dataSnapshot.getRef().child("calendar").setValue(calendar.get(Calendar.DAY_OF_MONTH) + "." + curMonth + "." + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                        Intent main = new Intent(NewTask.this, MainActivity.class);

                        if (calendar.after(Calendar.getInstance())) {
                            Log.w("NewTask", "Time im millis for notification: " + String.valueOf(calendar.getTimeInMillis() / 1000));

                            Intent notifyIntent = new Intent(NewTask.this, NotificationBroadcast.class);
                            Log.w("NewTask", "Title: " + title.getText().toString());
                            Log.w("NewTask", "Desc: " + description.getText().toString());
                            Log.w("NewTask", "ID: " + Integer.toString(key));
                            notifyIntent.putExtra("title", title.getText().toString());
                            notifyIntent.putExtra("description", description.getText().toString());
                            notifyIntent.putExtra("key", key);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(NewTask.this, key, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Calendar time = Calendar.getInstance();
                            long temp = (calendar.getTimeInMillis() - time.getTimeInMillis()) / 1000;
                            Log.w("NewTask", "Time now: " + String.valueOf(time.getTimeInMillis() / 1000));
                            Log.w("NewTask", "Time diff: " + String.valueOf(temp));
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        startActivity(main);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                //===========================================NOTIFICATIONS=================================================================================================
                notification = new NotificationCompat.Builder(NewTask.this, CHANNEL_ID_1)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(title.getText().toString())
                        .setContentText(description.getText().toString()).build();

                //   notificationManager.notify(key, notification);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(NewTask.this, MainActivity.class);
                startActivity(cancel);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_1, "Channel 1", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("desc");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }
}
