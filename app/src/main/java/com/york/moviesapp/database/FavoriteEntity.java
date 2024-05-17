package com.york.moviesapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favorite")
public class FavoriteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
