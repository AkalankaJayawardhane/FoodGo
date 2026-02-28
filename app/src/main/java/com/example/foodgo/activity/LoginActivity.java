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

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin;
    TextView txtRegisterLink;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegisterLink = findViewById(R.id.txtRegisterLink);

        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {

            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password)) {

                Toast.makeText(LoginActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Email format validation
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
            } else {
                Toast.makeText(LoginActivity.this,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        txtRegisterLink.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,
                        FoodGo.RegisterActivity.class)));
    }
}