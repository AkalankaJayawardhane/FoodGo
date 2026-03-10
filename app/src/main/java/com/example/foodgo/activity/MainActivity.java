package com.example.foodgo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodgo.R;
import com.example.foodgo.fragments.CartFragment;
import com.example.foodgo.fragments.HomeFragment;
import com.example.foodgo.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


 // Main Activity with Bottom Navigation
 // Hosts fragments for Home, Search, Cart, and Profile

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is logged in
        checkLoginStatus();

        // Initialize views
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Set up bottom navigation listener
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    loadFragment(new HomeFragment());
                    return true;


                    // Navigate to Home layout and focus search
                } else if (itemId == R.id.nav_search) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    if (currentFragment instanceof HomeFragment) {
                        ((HomeFragment) currentFragment).focusSearch();
                    } else {
                        HomeFragment homeFragment = new HomeFragment();
                        Bundle args = new Bundle();
                        args.putBoolean("focus_search", true);
                        homeFragment.setArguments(args);
                        loadFragment(homeFragment);
                    }
                    return true;

                    // Navigate to Cart layout
                } else if (itemId == R.id.nav_cart) {
                    loadFragment(new CartFragment());
                    return true;

                    // Navigate to Profile layout
                } else if (itemId == R.id.nav_profile) {
                    loadFragment(new ProfileFragment());
                    return true;
                }

                return false;
            }
        });

        // This ensures that when the app starts up, the HomeFragment is loaded by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }


     //Check if user is logged in, redirect to Login if not
    private void checkLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // User not logged in, redirect to Login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


     //Load fragment into container
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh current fragment if needed (e.g., cart updates)
    }
}