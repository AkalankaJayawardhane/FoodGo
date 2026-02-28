package FoodGo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText editName, editEmail, editPassword, editConfirmPassword;
    Button btnRegister;
    TextView txtLoginLink;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLoginLink = findViewById(R.id.txtLoginLink);

        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {

            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            // Empty validation
            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(confirmPassword)) {

                Toast.makeText(RegisterActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Email format validation
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(RegisterActivity.this,
                        "Enter a valid email address",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Password match validation
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Duplicate email check
            if (db.emailExists(email)) {
                Toast.makeText(RegisterActivity.this,
                        "Email already exists",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = db.registerUser(name, email, password);

            if (inserted) {
                Toast.makeText(RegisterActivity.this,
                        "Registration Successful",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(RegisterActivity.this,
                        LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this,
                        "Registration Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        txtLoginLink.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this,
                        LoginActivity.class)));
    }
}