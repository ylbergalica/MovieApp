    package com.york.moviesapp.database;

    import androidx.room.Dao;
    import androidx.room.Delete;
    import androidx.room.Insert;
    import androidx.room.OnConflictStrategy;
    import androidx.room.Query;


    import java.util.List;
    @Dao
    public interface MovieDao
    {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void save(MovieEntity data);

        @Delete
        void delete(MovieEntity data);

        @Query("SELECT * FROM Movie")
        List<MovieEntity> getAllMovies();
    }
