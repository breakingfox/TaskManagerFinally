package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class TypeAdd extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    RecyclerView recyclerView; //прокручиваемый список
    ArrayList<String> types; //массив для хранения типов
    SharedPreferences sp; //хранилище
    SharedPreferences.Editor editor; //объект для изменения хранилища
    Button addNewType, btnBack; //кнопки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_add);

        types = new ArrayList<>();
        sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE); ////получаем доступ к хранилищу
        editor = sp.edit(); //объект едитор
        int size = sp.getInt("Types_size", 0); //значение ключа (количество типов)
        for(int i=0;i<size;i++)
        {
            types.add(sp.getString("Types_" + i+1 , null)); //вводим в массив элементы их хранилища
        }


        String firstStart = sp.getString("first1","0"); //создание высплывающих сообщение
        if(types.size() == 0) // в случае если заметок нуль
        {
            Toast.makeText(this,"Добавьте новый тип задачи",Toast.LENGTH_LONG).show();
        }
        else if (firstStart.equalsIgnoreCase("0")) //если первый запуск данного активити
        {
            Toast.makeText(this, "Для удаления типа задачи смахните вправо/влево",Toast.LENGTH_LONG).show();
            editor.remove("first1");
            editor.putString("first1","1"); //меняем значение, следующий запуск будет не первым
            editor.apply();
        }


        recyclerView = findViewById(R.id.recycle_types);
        //заполняем прокручиваемый список элементами
        final TypeAdapter typeAdapter = new TypeAdapter(TypeAdd.this,types);
        recyclerView.setAdapter(typeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter.notifyDataSetChanged();

        //ItemTouchHelper позволяет осуществить функцию swipe-to-dismiss
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0 , ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //если произойдет свайп вправо/влево
                for(int i=0;i<types.size();i++)
                {
                    editor.remove("Types_" + i); //удаляем все старые элемент из хранилища
                }
                types.remove(viewHolder.getAdapterPosition()); //удаляем их массива элемент, который свайпнули
                for(int i=0;i<types.size();i++)
                {
                    editor.putString("Types_" + i, types.get(i)); //обновляем элементы хранилища
                }
                typeAdapter.notifyDataSetChanged();
                editor.remove("Types_size");
                editor.putInt("Types_size", types.size()); //меняем количество элементов
                editor.apply();
            }
        }).attachToRecyclerView(recyclerView); //указываем соответствующий прокручиваемый список

        btnBack  = findViewById(R.id.back_to_main);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(TypeAdd.this, MainActivity.class);
                startActivity(a);
            }
        });

        addNewType = findViewById(R.id.add_new_type);
        addNewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(TypeAdd.this, AddNewTypeTask.class);
                startActivity(a);
            }
        });
    }
}
