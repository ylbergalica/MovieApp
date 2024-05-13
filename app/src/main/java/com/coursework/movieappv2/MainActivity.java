package com.coursework.movieappv2;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.coursework.movieappv2.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Define button text list
        String[] buttonTexts = new String[]{
                "Button 1",
                "Button 2",
                "Button 3",
                "Button 4",
                "Button 5"
        };

        // Initialize buttons array
        buttons = new Button[buttonTexts.length];

        // Create buttons using a loop
        for (int i = 0; i < buttonTexts.length; i++) {
            buttons[i] = new Button(this);
            buttons[i].setText(buttonTexts[i]);
        }

        addButtonsToLayout();

        // Button click listener to fetch movies
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fetchMovies();
                } catch (AuthFailureError e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void addButtonsToLayout() {
        LinearLayout layout = findViewById(R.id.main); // Assuming your LinearLayout ID is "container"

        // Add buttons to the layout
        for (Button button : buttons) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 16, 0, 0); // Set margins (left, top, right, bottom) as needed
            button.setLayoutParams(layoutParams);

            layout.addView(button); // Add the button to the LinearLayout
        }
    }

    private void fetchMovies() throws AuthFailureError {
        TextView text = findViewById(R.id.text);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://app-vpigadas.herokuapp.com/api/movies/";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        text.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        text.setText(error.toString());
                    }
                });

        jsonObjectRequest.getHeaders().put("Content-Type", "application/json");

        queue.add(jsonObjectRequest);
    }
}