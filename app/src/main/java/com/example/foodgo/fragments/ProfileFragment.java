package com.example.foodgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodgo.R;
import com.example.foodgo.activity.LoginActivity;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.User;

/**
 * Profile Fragment - User profile screen
 * Displays user information and logout option
 */
public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Load user data
        loadUserData();

        // Logout button click
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    /**
     * Load user data from SharedPreferences
     */
    private void loadUserData() {

        SharedPreferences prefs = getContext()
                .getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            User user = dbHelper.getUserById(userId);

            if (user != null) {
                tvUserName.setText(user.getName());
                tvUserEmail.setText(user.getEmail());
            }
        }
    }

    /**
     * Logout user and navigate to Login screen
     */
    private void logout() {
        // Clear session
        SharedPreferences prefs = getContext().getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();

        // Navigate to Login Activity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}