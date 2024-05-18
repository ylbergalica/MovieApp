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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
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
import com.york.moviesapp.recyclerview.GenresViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {


private FragmentDashboardBinding binding;
private MovieDao movieDao;
private MovieDatabase movieDatabase;
private RecyclerView recyclerView;
private ArrayList<Category> categoryList;
private ArrayList<MovieEntity> movieList;

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

                    recyclerView.setAdapter(new GenresViewAdapter(movieList, categoryList, getActivity(), DashboardFragment.this, getLayoutInflater(), new GenresViewAdapter.OnMovieClickListener() {
                        @Override
                        public void onMovieClick(com.york.moviesapp.database.MovieEntity movie) {
                            // Handle movie click
                            Bundle bundle = new Bundle();
                            bundle.putInt("movieId", movie.getId());
                            NavHostFragment.findNavController(DashboardFragment.this)
                                    .navigate(R.id.action_dashboard_to_details, bundle);
                        }
                    }));
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle failure
                    Toast.makeText(getContext(), "Something went wrong, data is loaded from cache..", Toast.LENGTH_SHORT).show();
                    getMoviesFromDatabase();
                    Log.e("APIRequest", errorMessage);

                }
            });
            Log.d("NETWORK", "Network is available");
        } else {
            Toast.makeText(getContext(), "No internet connection data is loaded from cache storage.", Toast.LENGTH_SHORT).show();
            getMoviesFromDatabase();
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

    private void getMoviesFromDatabase() {
        new GetMoviesFromDatabaseTask().execute();
    }

    private class GetMoviesFromDatabaseTask extends AsyncTask<Void, Void, List<MovieEntity>> {
        @Override
        protected List<MovieEntity> doInBackground(Void... voids) {
            return movieDatabase.movieDao().getAllMovies();
        }

        @Override
        protected void onPostExecute(List<MovieEntity> movieEntities) {
            super.onPostExecute(movieEntities);
            if (movieEntities != null) {
                // Handle the list of movies retrieved from the database
                // create catogories from the list of movies
                for (MovieEntity movie : movieEntities) {
                    String[] genreIds = movie.getGenreIds().replace("[", "").replace("]", "").replace(" ", "").split(",");
                    for (String genreId : genreIds) {
                        boolean found = false;
                        if (categoryList == null) {
                            categoryList = new ArrayList<Category>();
                        }
                        for (Category category : categoryList) {
                            if (category.getId() == Integer.parseInt(genreId)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            Category category = new Category(Integer.parseInt(genreId));
                            categoryList.add(category);
                        }
                    }
                }
                recyclerView.setAdapter(new GenresViewAdapter(movieEntities, categoryList, getActivity(), DashboardFragment.this, getLayoutInflater(), new GenresViewAdapter.OnMovieClickListener() {
                    @Override
                    public void onMovieClick(com.york.moviesapp.database.MovieEntity movie) {
                        // Handle movie click
                        Bundle bundle = new Bundle();
                        bundle.putInt("movieId", movie.getId());
                        NavHostFragment.findNavController(DashboardFragment.this)
                                .navigate(R.id.action_dashboard_to_details, bundle);
                    }
                }));
            } else {
                // Handle the case when no movies are found in the database
                Log.e("GetMoviesTask", "No movies found in the database");
            }
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
                        Category category = new Category(genreId);
                        categoryList.add(category);
                    }
                }

                movieEntity.setDate(jsonObject.get("release_date").getAsString());
                movieEntity.setOverview(jsonObject.get("overview").getAsString());
                movieEntity.setBackdropPath(jsonObject.get("backdrop_path").getAsString());
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