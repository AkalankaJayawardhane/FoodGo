package com.example.foodgo.activity;

import android.content.Intent;
import android.content.SharedPreferences; // ✅ ADDED
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgo.R;
import com.example.foodgo.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin;
    TextView txtRegisterLink;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterLink = findViewById(R.id.txtRegisterLink);

        db = new DatabaseHelper(this);

        // Login Button Click
        btnLogin.setOnClickListener(v -> {

            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Check empty fields
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this,
                        "Enter a valid email",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            boolean valid = db.checkUser(email, password);

            if (valid) {

                Toast.makeText(LoginActivity.this,
                        "Login Successful",
                        Toast.LENGTH_SHORT).show();

                // 🔥 Get userId
                int userId = db.getUserIdByEmail(email);

                SharedPreferences prefs =
                        getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("userId", userId);   // 👈 ADD THIS LINE
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Register Text Click
        txtRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}