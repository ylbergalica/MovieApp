package com.york.moviesapp.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.york.moviesapp.databinding.HolderItemBinding;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    public HolderItemBinding binding;
    public MyRecyclerViewHolder(@NonNull HolderItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
