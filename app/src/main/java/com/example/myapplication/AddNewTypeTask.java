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

//активити для создания новых типов заметок
public class AddNewTypeTask extends AppCompatActivity {
    public static final String SHARED_PREFS = "prefs";
    Button btnSaveType; //кнопка сохранения нового типа
    EditText editType; //поле для ввода
    SharedPreferences sp; //постоянное хранилище для сохранения типов
    SharedPreferences.Editor mEdit1; //объект для изменения объектов хранилиша
    int size; //количество созданных типов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_type_task);

        sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE); //получаем доступ к хранилищу
        mEdit1 = sp.edit(); //получаем объект едитор
        btnSaveType = findViewById(R.id.btn_save_type);
        editType = findViewById(R.id.edit_type);
        size = sp.getInt("Types_size", 0); //достаем их хранилища значение ключа

        btnSaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editType.getText().toString(); //переводим в стринг данные из поля ввода
                if(text.isEmpty()) //если поле пустое, то создается всплывающее сообщение
                {
                    Toast.makeText(getApplicationContext(),"Тип без имени!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mEdit1.remove("Types_size"); //удаляем значение ключа
                    mEdit1.putInt("Types_size", size + 1); //задаём новое значение для ключа
                    mEdit1.putString("Types_" + size + 1, text); //помещаем новый тип в хранилище
                    mEdit1.apply(); //фиксируем изменения
                }
                Intent a = new Intent(AddNewTypeTask.this, TypeAdd.class);
                startActivity(a); //переходим в другое активити
            }
        });
    }
}
