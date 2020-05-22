package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewTypeTask extends AppCompatActivity {
    public static final String SHARED_PREFS = "prefs";
    Button btnSaveType;
    EditText editType;
    SharedPreferences sp;
    SharedPreferences.Editor mEdit1;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type_task);

        sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        mEdit1 = sp.edit();
        btnSaveType = findViewById(R.id.btn_save_type);
        editType = findViewById(R.id.edit_type);
        size = sp.getInt("Types_size", 0);

        btnSaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editType.getText().toString();
                if(text.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Тип без имени!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mEdit1.remove("Types_size");
                    mEdit1.putInt("Types_size", size + 1);
                    mEdit1.putString("Types_" + size + 1, text);
                    mEdit1.apply();
                }
                Intent a = new Intent(AddNewTypeTask.this, TypeAdd.class);
                startActivity(a);
            }
        });
    }
}
