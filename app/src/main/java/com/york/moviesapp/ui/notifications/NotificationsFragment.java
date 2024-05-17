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

import com.york.moviesapp.database.FavoriteDao;
import com.york.moviesapp.database.FavoriteEntity;
import com.york.moviesapp.database.MovieDatabase;
import com.york.moviesapp.databinding.FragmentNotificationsBinding;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private FavoriteDao favoriteDao;
    private MovieDatabase movieDatabase;
private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

    binding = FragmentNotificationsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        movieDatabase = MovieDatabase.getInstance(getContext());
        favoriteDao = movieDatabase.favoriteDao();

        getAllFavoriteMovies();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

private void getAllFavoriteMovies(){
        new getAllFavoriteMoviesAsyncTask().execute();
}

private class getAllFavoriteMoviesAsyncTask extends AsyncTask<Void, Void, List<FavoriteEntity>> {
        @Override
        protected List<FavoriteEntity> doInBackground(Void... voids) {
            return favoriteDao.getFavoriteMovies();
        }

        @Override
        protected void onPostExecute(List<FavoriteEntity> favoriteEntities) {
            super.onPostExecute(favoriteEntities);

            if (favoriteEntities != null) {
                for (FavoriteEntity favoriteEntity : favoriteEntities) {
                    Log.d("Favorite Movies", favoriteEntity.getId() + " " + favoriteEntity.getMovieId());
                }
            }
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}