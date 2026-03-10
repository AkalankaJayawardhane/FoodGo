package com.example.foodgo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
import com.example.foodgo.activity.FoodDetailActivity;
import com.example.foodgo.adapters.CategoryAdapter;
import com.example.foodgo.adapters.FoodAdapter;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.FoodItem;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Home Fragment - Displays popular food items
public class HomeFragment extends Fragment {

    private RecyclerView rvCategories, rvPopularItems;
    private TextView tvSectionTitle;
    private EditText etSearch;

    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private DatabaseHelper dbHelper;

    private List<String> categories;
    private List<FoodItem> allFoodItems;
    private String selectedCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize database
        dbHelper = new DatabaseHelper(getContext());

        // Initialize views
        rvCategories = view.findViewById(R.id.rvCategories);
        rvPopularItems = view.findViewById(R.id.rvPopularItems);
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle);
        etSearch = view.findViewById(R.id.etSearch);

        // search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoodItemsBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set up categories
        categories = Arrays.asList("All", "Burger", "Wings", "Pizza");
        setupCategoriesRecyclerView();

        // Load food items
        // Load food items
        loadFoodItems();

        // Check if we need to focus search
        if (getArguments() != null && getArguments().getBoolean("focus_search", false)) {
            etSearch.post(new Runnable() {
                @Override
                public void run() {
                    focusSearch();
                }
            });
        }

        return view;
    }

    // Set up categories RecyclerView
    private void setupCategoriesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false);
        rvCategories.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(getContext(), categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(String category, int position) {
                selectedCategory = category;
                filterFoodItems(category);
            }
        });

        rvCategories.setAdapter(categoryAdapter);
    }

    // Load food items from database
    private void loadFoodItems() {
        allFoodItems = dbHelper.getAllFoodItems();
        setupPopularItemsRecyclerView();
    }

    // Set up popular items RecyclerView
    private void setupPopularItemsRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvPopularItems.setLayoutManager(gridLayoutManager);

        foodAdapter = new FoodAdapter(getContext(), allFoodItems, new FoodAdapter.OnFoodClickListener() {
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

        rvPopularItems.setAdapter(foodAdapter);
    }

    // Filter food items by category
    private void filterFoodItems(String category) {
        List<FoodItem> filteredItems;

        if (category.equals("All")) {
            filteredItems = allFoodItems;
            if (tvSectionTitle != null)
                tvSectionTitle.setText(R.string.popular_title);
        } else {
            filteredItems = dbHelper.getFoodItemsByCategory(category);
            if (tvSectionTitle != null)
                tvSectionTitle.setText(category + " Items");
        }

        if (foodAdapter != null) {
            foodAdapter.updateList(filteredItems);
        }
        if (foodAdapter != null) {
            foodAdapter.updateList(filteredItems);
        }
    }

    // Filter food items by search query
    private void filterFoodItemsBySearch(String query) {
        List<FoodItem> filteredList = new ArrayList<>();

        // Reset category selection visual if searching
        if (!query.isEmpty() && tvSectionTitle != null) {
            tvSectionTitle.setText("Results for \"" + query + "\"");
        } else if (query.isEmpty() && tvSectionTitle != null) {
            tvSectionTitle.setText(R.string.popular_title);
        }

        for (FoodItem item : allFoodItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (foodAdapter != null) {
            foodAdapter.updateList(filteredList);
        }
    }

    // Add Food items to cart
    private void addToCart(FoodItem foodItem) {
        SharedPreferences prefs = getContext().getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            dbHelper.addToCart(userId, foodItem.getId(), 1);
            Toast.makeText(getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data if needed
    }

    // Focus search
    public void focusSearch() {
        if (etSearch != null) {
            etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}