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

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    private ArrayList<String> taskTypes = new ArrayList<>();
    private Context context;

    public TypeAdapter(Context context, ArrayList<String> taskTypes)
    {
        this.context  = context;
        this.taskTypes = taskTypes;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type,parent,false);
        TypeViewHolder typeViewHolder = new TypeViewHolder(view);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
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
