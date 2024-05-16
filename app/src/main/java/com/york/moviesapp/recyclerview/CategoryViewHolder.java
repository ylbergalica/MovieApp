package com.york.moviesapp.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.york.moviesapp.databinding.MovieItemBinding;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public MovieItemBinding binding;
    public CategoryViewHolder(@NonNull MovieItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
