package com.york.moviesapp.recyclerview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.york.moviesapp.databinding.ActivityRecyclerBinding;

public class RecyclerActivity extends AppCompatActivity {

    public ActivityRecyclerBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityRecyclerBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        _binding.recyclerView.setAdapter(
//                new GenresViewAdapter(new ArrayList<String>(), this, , getLayoutInflater())
//        );
    }
}