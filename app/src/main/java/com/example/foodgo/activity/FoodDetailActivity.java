package com.example.foodgo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodgo.R;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.FoodItem;
import com.example.foodgo.database.DatabaseHelper;

/**
 * Food Detail Activity - Detailed view of a food item
 * Allows user to view details and add item to cart with quantity
 */
public class FoodDetailActivity extends AppCompatActivity {

    private ImageView ivFoodImage, btnDecrement, btnIncrement;
    private TextView tvFoodName, tvFoodPrice, tvFoodDescription, tvQuantity;
    //private Button btnAddToCart;

    private DatabaseHelper dbHelper;
    private FoodItem foodItem;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        ivFoodImage = findViewById(R.id.ivFoodImage);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodPrice = findViewById(R.id.tvFoodPrice);
        tvFoodDescription = findViewById(R.id.tvFoodDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrement = findViewById(R.id.btnDecrement);
        btnIncrement = findViewById(R.id.btnIncrement);
       // btnAddToCart = findViewById(R.id.btnAddToCart);

        // Get food ID from intent
        int foodId = getIntent().getIntExtra("food_id", -1);
        if (foodId != -1) {
            loadFoodDetails(foodId);
        }

        // Set up quantity controls
        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    tvQuantity.setText(String.valueOf(quantity));
                }
            }
        });

    /*   // Add to cart button
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        */
    }

    /**
     * Load food details from database
     */
    private void loadFoodDetails(int foodId) {
        foodItem = dbHelper.getFoodItemById(foodId);

        if (foodItem != null) {
            tvFoodName.setText(foodItem.getName());
            tvFoodPrice.setText(foodItem.getPrice() + " RS");
            tvFoodDescription.setText(foodItem.getDescription());
            ivFoodImage.setImageResource(foodItem.getImageResource());
        }
    }


   /* private void addToCart() {
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1 && foodItem != null) {
            dbHelper.addToCart(userId, foodItem.getId(), quantity);
            Toast.makeText(this, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
            finish(); // Return to previous screen
        }
    }
    */

}
