package com.york.moviesapp.ui.dashboard;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.york.moviesapp.R;
import com.york.moviesapp.database.Category;
import com.york.moviesapp.database.MovieDao;
import com.york.moviesapp.database.MovieDatabase;
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.FragmentDashboardBinding;
import com.york.moviesapp.helpers.APIRequest;
import com.york.moviesapp.recyclerview.MyRecyclerViewAdapter;
import com.york.moviesapp.ui.home.HomeFragment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {


private FragmentDashboardBinding binding;
private MovieDao movieDao;
private MovieDatabase movieDatabase;
private RecyclerView recyclerView;
private ArrayList<Category> categoryList;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        movieDatabase = MovieDatabase.getInstance(getContext());
        movieDao = movieDatabase.movieDao();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        if(isNetworkAvailable(getActivity())) {
            APIRequest.getMovies(new APIRequest.ApiCallback() {
                @Override
                public void onSuccess(JsonElement data) {
                    // Process the data here
                    Log.d("DATA", data.toString());
                    List<MovieEntity> movieList = parseData(data.getAsJsonObject().get("results").getAsJsonArray());
                    insertMoviesIntoDatabase(movieList);

                    recyclerView.setAdapter(new MyRecyclerViewAdapter(data, getActivity(), DashboardFragment.this, getLayoutInflater()));
                }


                @Override
                public void onFailure(String errorMessage) {
                    // Handle failure
                    Log.e("APIRequest", errorMessage);
                }
            });
            Log.d("NETWORK", "Network is available");
        } else {
            Toast.makeText(getContext(), "No internet connection data is loaded from cache storage.", Toast.LENGTH_SHORT).show();
            Log.d("NETWORK", "Network is not available");
        }




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

    private void insertMoviesIntoDatabase(List<MovieEntity> movieList) {
        new InsertMoviesTask().execute(movieList);
    }

    private class InsertMoviesTask extends AsyncTask<List<MovieEntity>, Void, Void> {
        @Override
        protected Void doInBackground(List<MovieEntity>... lists) {
            List<MovieEntity> movieList = lists[0];
            if (movieList != null && !movieList.isEmpty()) {
                for (MovieEntity movie : movieList) {
                    movieDao.save(movie);
                }
            }
            return null;
        }
    }

    private List<MovieEntity> parseData(JsonArray data) {

        ArrayList<MovieEntity> dataList = new ArrayList<MovieEntity>();

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
        return dataList;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}