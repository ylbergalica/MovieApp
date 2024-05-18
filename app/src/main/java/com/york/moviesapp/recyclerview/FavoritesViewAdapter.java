package com.york.moviesapp.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.FavoritesItemBinding;

import java.util.ArrayList;

public class FavoritesViewAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {
    private ArrayList<MovieEntity> dataList;
    private Context context;
    private LayoutInflater inflate;
    private GenresViewAdapter.OnMovieClickListener listener;

    public FavoritesViewAdapter(ArrayList<MovieEntity> data, Context context, LayoutInflater inflate, GenresViewAdapter.OnMovieClickListener listener) {
        this.context = context;
        this.inflate = inflate;
        this.dataList = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.holder_item, parent, false);
        FavoritesItemBinding binding = FavoritesItemBinding.inflate(inflate, parent, false);
        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        // set text
        holder.binding.textView.setText(dataList.get(position).getTitle());

        // set image
        ImageView imageView = holder.binding.image;
        Glide.with(context).load(dataList.get(position).getPosterPath()).into(imageView);

        holder.binding.getRoot().setOnClickListener(v -> {
            listener.onMovieClick(dataList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
