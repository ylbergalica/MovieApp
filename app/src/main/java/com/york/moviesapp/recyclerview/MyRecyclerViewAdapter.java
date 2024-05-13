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
import com.york.moviesapp.databinding.HolderItemBinding;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private ArrayList<MovieEntity> dataList;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflate;

    public MyRecyclerViewAdapter(JsonElement data, Context context, Fragment fragment, LayoutInflater inflate) {
        this.context = context;
        this.fragment = fragment;
        this.inflate = inflate;

        JsonArray results = data.getAsJsonObject().get("results").getAsJsonArray();
        parseData(results);
    }

    private void parseData(JsonArray data) {
        dataList = new ArrayList<MovieEntity>();

        if (data != null && data.isJsonArray()) {
            for (JsonElement element : data) {
                JsonObject jsonObject = element.getAsJsonObject();
                MovieEntity movieEntity = new MovieEntity();
                movieEntity.setId(jsonObject.get("id").getAsInt());
                movieEntity.setTitle(jsonObject.get("title").getAsString());
                movieEntity.setPosterPath(jsonObject.get("poster_path").getAsString());
                movieEntity.setDate(jsonObject.get("release_date").getAsString());
                movieEntity.setOverview(jsonObject.get("overview").getAsString());
                movieEntity.setPopularity(jsonObject.get("popularity").getAsFloat());
                movieEntity.setVoteCount(jsonObject.get("vote_count").getAsInt());
                movieEntity.setVoteAverage(jsonObject.get("vote_average").getAsFloat());
                dataList.add(movieEntity);

                Log.d("da", movieEntity.getTitle());
            }
        }
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
        holder.binding.text.setText(dataList.get(position).getTitle());

        ImageView imageView = holder.binding.image;
        Glide.with(context).load(dataList.get(position).getPosterPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
