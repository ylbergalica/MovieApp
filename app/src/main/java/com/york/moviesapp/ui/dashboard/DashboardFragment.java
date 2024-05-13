package com.york.moviesapp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.york.moviesapp.R;
import com.york.moviesapp.databinding.FragmentDashboardBinding;
import com.york.moviesapp.helpers.APIRequest;
import com.york.moviesapp.recyclerview.MyRecyclerViewAdapter;
import com.york.moviesapp.ui.home.HomeFragment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {


private FragmentDashboardBinding binding;
private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        APIRequest.getMovies(new APIRequest.ApiCallback() {
            @Override
            public void onSuccess(JsonElement data) {
                // Process the data here
                Log.d("DATA", data.toString());

                recyclerView.setAdapter(new MyRecyclerViewAdapter(data, getActivity(), DashboardFragment.this, getLayoutInflater()));
            }


            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
                Log.e("APIRequest", errorMessage);
            }
        });


        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}