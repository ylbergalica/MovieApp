package com.york.moviesapp.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.york.moviesapp.databinding.HolderItemBinding;

public class GenresViewHolder extends RecyclerView.ViewHolder {
    public HolderItemBinding binding;
    public GenresViewHolder(@NonNull HolderItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
