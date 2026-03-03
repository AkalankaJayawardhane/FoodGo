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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
// import com.example.foodgo.activity.CheckoutActivity;
import com.example.foodgo.activity.CheckoutActivity;
import com.example.foodgo.adapters.CartAdapter;
import com.example.foodgo.database.DatabaseHelper;
import com.example.foodgo.models.CartItem;

import java.util.List;

/**
 * Cart Fragment - Shopping cart screen
 * Displays cart items with quantity controls and total price
 */
public class CartFragment extends Fragment {

    private RecyclerView rvCartItems;
    private TextView tvEmptyCart, tvTotalPrice;
    private Button btnCheckout;

    private CartAdapter cartAdapter;
    private DatabaseHelper dbHelper;
    private List<CartItem> cartItems;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize database
        dbHelper = new DatabaseHelper(getContext());

        // Get user ID
        SharedPreferences prefs = getContext().getSharedPreferences("FoodAppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        // Initialize views
        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        // Load cart items
        loadCartItems();

        // Checkout button click
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems != null && !cartItems.isEmpty()) {
                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), R.string.cart_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /**
     * Load cart items from database
     */
    private void loadCartItems() {
        if (userId != -1) {
            cartItems = dbHelper.getCartItems(userId);

            if (cartItems.isEmpty()) {
                // Show empty cart message
                rvCartItems.setVisibility(View.GONE);
                tvEmptyCart.setVisibility(View.VISIBLE);
                tvTotalPrice.setText("0 RS");
            } else {
                // Show cart items
                rvCartItems.setVisibility(View.VISIBLE);
                tvEmptyCart.setVisibility(View.GONE);
                setupRecyclerView();
                updateTotalPrice();
            }
        }
    }

    /**
     * Set up cart RecyclerView
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvCartItems.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(getContext(), cartItems, new CartAdapter.OnCartItemListener() {
            @Override
            public void onQuantityChanged(CartItem cartItem, int newQuantity) {
                // Update quantity in database
                dbHelper.updateCartItemQuantity(cartItem.getId(), newQuantity);
                updateTotalPrice();
                Toast.makeText(getContext(), R.string.cart_updated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemRemoved(CartItem cartItem) {
                // Remove from database
                dbHelper.removeFromCart(cartItem.getId());
                cartItems.remove(cartItem);
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
                Toast.makeText(getContext(), R.string.item_removed, Toast.LENGTH_SHORT).show();

                // Check if cart is empty
                if (cartItems.isEmpty()) {
                    rvCartItems.setVisibility(View.GONE);
                    tvEmptyCart.setVisibility(View.VISIBLE);
                }
            }
        });

        rvCartItems.setAdapter(cartAdapter);
    }

    /**
     * Update total price display
     */
    private void updateTotalPrice() {
        int total = dbHelper.getCartTotal(userId);
        tvTotalPrice.setText(total + " RS");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload cart items when returning to this fragment
        loadCartItems();
    }
}