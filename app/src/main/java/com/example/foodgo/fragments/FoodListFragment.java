package com.example.foodgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
import com.example.foodgo.activity.FoodDetailActivity;
import com.example.foodgo.adapters.FoodAdapter;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.FoodItem;
import com.example.foodgo.adapters.FoodAdapter;
import com.example.foodgo.database.DatabaseHelper;

import java.util.List;

/**
 * Food List Fragment - Displays filtered food items
 * Shows vertical list of food items for a specific category
 */
public class FoodListFragment extends Fragment {

    private RecyclerView rvFoodList;
    private FoodAdapter foodAdapter;
    private DatabaseHelper dbHelper;

    private String category;

    public static FoodListFragment newInstance(String category) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);

        // Initialize database
        dbHelper = new DatabaseHelper(getContext());

        // Get category from arguments
        if (getArguments() != null) {
            category = getArguments().getString("category", "All");
        }

        // Initialize views
        rvFoodList = view.findViewById(R.id.rvFoodList);

        // Load food items
        loadFoodItems();

        return view;
    }

    /**
     * Load food items by category
     */
    private void loadFoodItems() {
        List<FoodItem> foodItems;

        if (category.equals("All")) {
            foodItems = dbHelper.getAllFoodItems();
        } else {
            foodItems = dbHelper.getFoodItemsByCategory(category);
        }

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFoodList.setLayoutManager(layoutManager);

        foodAdapter = new FoodAdapter(getContext(), foodItems, new FoodAdapter.OnFoodClickListener() {
            @Override
            public void onFoodClick(FoodItem foodItem) {
                // Navigate to Food Detail Activity
                Intent intent = new Intent(getContext(), FoodDetailActivity.class);
                intent.putExtra("food_id", foodItem.getId());
                startActivity(intent);
            }

            @Override
            public void onAddClick(FoodItem foodItem) {
                // Add to cart directly
                addToCart(foodItem);
            }
        });

        rvFoodList.setAdapter(foodAdapter);
    }

    /**
     * Add food item to cart
     */
    private void addToCart(FoodItem foodItem) {
        SharedPreferences prefs = getContext().getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            dbHelper.addToCart(userId, foodItem.getId(), 1);
            Toast.makeText(getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
        }
    }
}