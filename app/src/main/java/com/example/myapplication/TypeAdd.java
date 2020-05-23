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
    RecyclerView recyclerView;
    ArrayList<String> types;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Button addNewType, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_add);

        types = new ArrayList<>();
        sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        editor = sp.edit();
        int size = sp.getInt("Types_size", 0);
        for(int i=0;i<size;i++)
        {
            types.add(sp.getString("Types_" + i+1 , null));
        }


        String firstStart = sp.getString("first1","0");
        if(types.size() == 0)
        {
            Toast.makeText(this,"Добавьте новый тип задачи",Toast.LENGTH_LONG).show();
        }
        else if (firstStart.equalsIgnoreCase("0"))
        {
            Toast.makeText(this, "Для удаления типа задачи смахните вправо/влево",Toast.LENGTH_LONG).show();
            editor.remove("first1");
            editor.putString("first1","1");
            editor.apply();
        }


        recyclerView = findViewById(R.id.recycle_types);

        final TypeAdapter typeAdapter = new TypeAdapter(TypeAdd.this,types);
        recyclerView.setAdapter(typeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter.notifyDataSetChanged();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0 , ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                for(int i=0;i<types.size();i++)
                {
                    editor.remove("Types_" + i);
                }
                types.remove(viewHolder.getAdapterPosition());
                for(int i=0;i<types.size();i++)
                {
                    editor.putString("Types_" + i, types.get(i));
                }
                typeAdapter.notifyDataSetChanged();
                editor.remove("Types_size");
                editor.putInt("Types_size", types.size());
                editor.apply();
            }
        }).attachToRecyclerView(recyclerView);

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
