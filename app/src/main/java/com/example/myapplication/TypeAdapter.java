package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

//данный адаптер позволяет установить свзяь между элементами java кода с View -компонентами для типов
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    private ArrayList<String> taskTypes = new ArrayList<>(); //массив элементов
    private Context context; //Контекст представляет данные среды

    public TypeAdapter(Context context, ArrayList<String> taskTypes)
    {
        this.context  = context;
        this.taskTypes = taskTypes;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //создание новых объектов ViewHolder всякий раз, когда RecyclerView нуждается в этом
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type,parent,false);
        TypeViewHolder typeViewHolder = new TypeViewHolder(view);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        //устанавливка необходимых данных для соответствующей строки во view-компоненте
        holder.typeName.setText(taskTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return taskTypes.size();
    }


    static class TypeViewHolder extends RecyclerView.ViewHolder
    {
        TextView typeName;
        RelativeLayout parent;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeName = itemView.findViewById(R.id.type_title);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
