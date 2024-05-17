package com.york.moviesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(FavoriteEntity favoriteEntity);

    @Query("DELETE FROM Favorite WHERE movieId = :movieId")
    void removeFavorite(int movieId);

    @Query("SELECT * FROM Favorite")
    List<FavoriteEntity> getFavoriteMovies();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE movieId = :movieId)")
    int isFavorite(int movieId);
}