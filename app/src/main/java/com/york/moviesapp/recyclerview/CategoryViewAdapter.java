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
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.MovieItemBinding;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private ArrayList<MovieEntity> dataList;
    private Context context;
    private LayoutInflater inflate;

    public CategoryViewAdapter(ArrayList<MovieEntity> data, Context context, LayoutInflater inflate) {
        this.context = context;
        this.inflate = inflate;
        this.dataList = data;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.holder_item, parent, false);
        MovieItemBinding binding = MovieItemBinding.inflate(inflate, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // set text
//        holder.binding.text.setText(dataList.get(position).getTitle());

        // set image
        ImageView imageView = holder.binding.image;
        Glide.with(context).load(dataList.get(position).getPosterPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
