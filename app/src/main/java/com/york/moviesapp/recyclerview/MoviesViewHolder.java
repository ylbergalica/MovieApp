package com.york.moviesapp.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.york.moviesapp.databinding.MovieItemBinding;

public class MoviesViewHolder extends RecyclerView.ViewHolder {
    public MovieItemBinding binding;
    public MoviesViewHolder(@NonNull MovieItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
