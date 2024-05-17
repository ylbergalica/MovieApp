package com.york.moviesapp.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.york.moviesapp.database.MovieDao;
import com.york.moviesapp.database.MovieDatabase;
import com.york.moviesapp.database.MovieEntity;
import com.york.moviesapp.databinding.FragmentNotificationsBinding;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private MovieDao movieDao;
    private MovieDatabase movieDatabase;
private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        movieDatabase = MovieDatabase.getInstance(getContext());
        movieDao = movieDatabase.movieDao();

        getFavoriteMovies();


        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void getFavoriteMovies() {
        new GetFavoriteMoviesAsyncTask().execute();
    }

    private class GetFavoriteMoviesAsyncTask extends AsyncTask<Void, Void, List<MovieEntity>> {
        @Override
        protected List<MovieEntity> doInBackground(Void... voids) {
            return movieDao.getFavoriteMovies();
        }

        @Override
        protected void onPostExecute(List<MovieEntity> movieEntities) {
            List<MovieEntity> movieList = movieEntities;
            // update UI
            Log.d("FAVORITE_MOVIES", movieList.toString());
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}