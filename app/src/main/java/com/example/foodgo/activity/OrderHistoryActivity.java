package com.example.foodgo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
import com.example.foodgo.adapters.OrderAdapter;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.Order;

import java.util.List;

// Order History Activity - Displays a list of orders for the user
public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private DatabaseHelper dbHelper;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize views
        rvOrders = findViewById(R.id.rvOrders);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Get user ID
        SharedPreferences prefs = getSharedPreferences("FoodAppPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        //Retrieve all orders belonging to the current user
        List<Order> orders = dbHelper.getOrdersByUser(userId);

        //Create adapter to bind order data to RecyclerView
        OrderAdapter adapter = new OrderAdapter(this, orders);

        // Set layout manager to arrange items vertically
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
    }
}