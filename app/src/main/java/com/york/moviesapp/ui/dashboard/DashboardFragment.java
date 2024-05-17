package com.york.moviesapp.ui.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
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

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        if (isNetworkAvailable(getContext())) {
            // Here we fetch from the api because we have internet
            Log.e("InternetCheck", "Network is not available");
        }else {
            // Here use tha cached data and show a message to the user

            //create a toast message saying that there is no internet
            Toast.makeText(getContext(), "No Internet Connection. Using cached data.", Toast.LENGTH_LONG).show();


            Log.d("InternetCheck", "Network is available");
        }

        APIRequest.getDemoMovies(new APIRequest.ApiCallback() {
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
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}



