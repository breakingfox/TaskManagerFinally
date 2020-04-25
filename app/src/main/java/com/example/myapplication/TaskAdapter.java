package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    Context context;
    ArrayList<TaskNode> taskList;

    public TaskAdapter(Context cont, ArrayList<TaskNode> task) {
        context = cont;
        taskList = task;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.title.setText(taskList.get(i).getTitle());
        holder.description.setText(taskList.get(i).getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        holder.calendar.setText(taskList.get(i).getCalendar());
        final String title = taskList.get(i).getTitle();
        final String description = taskList.get(i).getDescription();
        final String key = Integer.toString(taskList.get(i).getKey());
        final String calendar = taskList.get(i).getCalendar();
        final String type = taskList.get(i).getType();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editing = new Intent(context,EditTask.class);
                editing.putExtra("title", title);
                editing.putExtra("description", description);
                editing.putExtra("key", key);
                editing.putExtra("calendar",calendar);
                editing.putExtra("type",type);
                context.startActivity(editing);
            }
        });
    }



    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description,  key,calendar, type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            calendar = (TextView) itemView.findViewById(R.id.calendar);
        }
    }
}
