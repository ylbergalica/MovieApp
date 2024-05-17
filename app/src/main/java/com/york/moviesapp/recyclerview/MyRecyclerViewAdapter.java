package com.york.moviesapp.recyclerview;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.york.moviesapp.database.Category;
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.HolderItemBinding;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {
    public interface OnMovieClickListener {
        void onMovieClick(MovieEntity movie);
    }

    private ArrayList<MovieEntity> dataList;
    private ArrayList<Category> categoryList;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflate;
    private OnMovieClickListener listener;

    public MyRecyclerViewAdapter(List<MovieEntity> dataList, ArrayList<Category> categories, Context context, Fragment fragment, LayoutInflater inflate, OnMovieClickListener listener) {
        this.context = context;
        this.fragment = fragment;
        this.inflate = inflate;
        this.listener = listener;
        this.dataList = new ArrayList<MovieEntity>(dataList);
        this.categoryList = categories;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.holder_item, parent, false);
        HolderItemBinding binding = HolderItemBinding.inflate(inflate, parent, false);
        return new MyRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        // set text
        holder.binding.text.setText(categoryList.get(position).getName());

        // make arraylist of movies in this category
        ArrayList<MovieEntity> categoryMovies = new ArrayList<MovieEntity>();
        for (MovieEntity movie : dataList) {
            if (movie.getGenreIds().contains(categoryList.get(position).getId() + "")) {
                categoryMovies.add(movie);
            }
        }

        holder.binding.categoryView.setAdapter(new CategoryViewAdapter(categoryMovies, context, inflate, listener));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
