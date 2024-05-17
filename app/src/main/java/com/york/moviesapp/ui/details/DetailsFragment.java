package com.york.moviesapp.ui.details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.york.moviesapp.R;
import com.york.moviesapp.database.MovieDao;
import com.york.moviesapp.database.MovieDatabase;
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.FragmentDetailsBinding;
import com.york.moviesapp.helpers.APIRequest;
import com.york.moviesapp.recyclerview.MyRecyclerViewAdapter;
import com.york.moviesapp.ui.home.HomeFragment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private FragmentDetailsBinding binding;
    private int movieId;

    private MovieDao movieDao;
    private MovieDatabase movieDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DetailsViewModel dashboardViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        movieDatabase = MovieDatabase.getInstance(getContext());
        movieDao = movieDatabase.movieDao();

        // movie title, release year, plot synopsis, genre, rating, runtime, director, cast
        TextView title = binding.title;
        ImageView image = binding.image;
        TextView releaseYear = binding.release;
        TextView plot = binding.description;
        TextView genres = binding.genres;
        TextView rating = binding.rating;
        TextView runtime = binding.runtime;
        TextView director = binding.director;
        TextView cast = binding.cast;

        ImageView favIcon = binding.favIcon;

        if(isNetworkAvailable(getActivity())) {
            if (getArguments() != null) {
                movieId = getArguments().getInt("movieId");
                // Use the movieId to fetch and display the movie details

                APIRequest.getMovie("" + movieId, new APIRequest.ApiCallback() {
                    @Override
                    public void onSuccess(JsonElement data) {
                        // Process the data here
                        Log.d("DATA", data.toString());

                        JsonObject obj = data.getAsJsonObject();

                        // movie title, release year, plot synopsis, genre, rating, runtime, director, cast
                        Glide.with(getContext()).load(obj.get("backdrop_path").getAsString()).into(image);
                        title.setText(obj.get("title").getAsString());
                        releaseYear.setText(obj.get("release_date").getAsString());

                        String genreString = "";
                        for (JsonElement genre : obj.get("genres").getAsJsonArray()) {
                            genreString += genre.getAsJsonObject().get("name").getAsString() + ", ";
                        }
                        genres.setText(genreString);

                        plot.setText(obj.get("overview").getAsString());
                        rating.setText("Rating: " + obj.get("vote_average").getAsString());
                        runtime.setText("Movie Length: " + obj.get("runtime").getAsString() + "m");

                        String directorString = "Directors: ";
                        for (JsonElement crewMember : obj.get("crew").getAsJsonArray()) {
                            if (crewMember.getAsJsonObject().get("department").getAsString().equals("Directing")) {
                                directorString += crewMember.getAsJsonObject().get("name").getAsString() + ", ";
                            }
                        }
                        director.setText(directorString);

                        String castString = "Cast: ";
                        for (JsonElement castMember : obj.get("cast").getAsJsonArray()) {
                            castString += castMember.getAsJsonObject().get("name").getAsString() + ", ";
                        }
                        cast.setText(castString);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure
                        Log.e("APIRequest", errorMessage);
                    }
                });
            } else {
                Log.e("APIRequest", "No movieId provided");
            }

            Log.d("NETWORK", "Network is available");
        } else {
            Toast.makeText(getContext(), "No internet connection data is loaded from cache storage.", Toast.LENGTH_SHORT).show();
            Log.d("NETWORK", "Network is not available");
        }

        // make fav icon clickable
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add movie to favorites
                Toast.makeText(getContext(), "Movie added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

    private void toggleFavorite(int id) {
        new toggleFavoriteAsyncTask().execute(id);
    }

    private class toggleFavoriteAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int id = integers[0];
            movieDao.toggleFavorite(id);
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}