package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder>
{

    private ArrayList<String> recycleData;

    public TypeAdapter(ArrayList<String> recycleData) {
        this.recycleData = recycleData;
    }

    @NonNull
    @Override
    public TypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}