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

//        JsonArray results = data.getAsJsonObject().get("results").getAsJsonArray();
//        parseData(results);
        this.dataList = new ArrayList<MovieEntity>(dataList);
        this.categoryList = categories;
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

                // parse genre_ids into ArrayList of integers
                JsonArray genreIds = jsonObject.get("genre_ids").getAsJsonArray();
                ArrayList<Integer> genreIdsList = new ArrayList<Integer>();
                for (JsonElement genreId : genreIds) {
                    genreIdsList.add(genreId.getAsInt());
                }
                movieEntity.setGenreIds(genreIdsList.toString());

                // add category to categoryList if its new
                for (int genreId : genreIdsList) {
                    boolean found = false;
                    if (categoryList == null) {
                        categoryList = new ArrayList<Category>();
                    }
                    for (Category category : categoryList) {
                        if (category.getId() == genreId) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Category category = new Category(genreId, "test" + genreId);
                        categoryList.add(category);
                    }
                }

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
