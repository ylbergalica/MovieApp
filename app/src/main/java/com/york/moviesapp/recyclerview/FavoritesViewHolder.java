package com.york.moviesapp.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.york.moviesapp.databinding.FavoritesItemBinding;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {
    public FavoritesItemBinding binding;
    public FavoritesViewHolder(@NonNull FavoritesItemBinding itemView) {
        super(itemView.getRoot());
        this.binding = itemView;
    }
}
