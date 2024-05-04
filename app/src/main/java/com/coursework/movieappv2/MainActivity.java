package com.coursework.movieappv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.coursework.movieappv2.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                }
            });

            layout.addView(button); // Add the button to the LinearLayout
        }
    }
}