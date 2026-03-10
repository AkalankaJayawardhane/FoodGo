package com.example.foodgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgo.R;


//Splash Activity - Shows app logo then opens MainActivity

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Navigate to MainActivity after delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs =
                        getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);

                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                Intent intent;

                // Check if user is logged in
                if (isLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish(); // close splash so user can't go back

            }
        }, SPLASH_DELAY);
    }
}