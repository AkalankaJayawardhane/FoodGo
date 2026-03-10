package com.example.foodgo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
import com.example.foodgo.models.CartItem;

import java.util.List;

 // Adapter for cart items RecyclerView
 // Displays cart items with quantity controls
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(CartItem cartItem, int newQuantity);

        void onItemRemoved(CartItem cartItem);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    // Bind cart item data to views
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.tvCartFoodName.setText(cartItem.getFoodItem().getName());
        holder.tvCartFoodPrice.setText(cartItem.getFoodItem().getPrice() + " RS");
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.ivCartFoodImage.setImageResource(cartItem.getFoodItem().getImageResource());

        // Increment quantity
        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(newQuantity);
                holder.tvQuantity.setText(String.valueOf(newQuantity));

                if (listener != null) {
                    listener.onQuantityChanged(cartItem, newQuantity);
                }
            }
        });

        // Decrement quantity
        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getQuantity() > 1) {
                    int newQuantity = cartItem.getQuantity() - 1;
                    cartItem.setQuantity(newQuantity);
                    holder.tvQuantity.setText(String.valueOf(newQuantity));

                    if (listener != null) {
                        listener.onQuantityChanged(cartItem, newQuantity);
                    }
                }
            }
        });

        // Remove item
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemRemoved(cartItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ViewHolder for cart items
    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCartFoodImage, btnIncrement, btnDecrement, btnRemove;
        TextView tvCartFoodName, tvCartFoodPrice, tvQuantity;

        // Initialize views
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartFoodImage = itemView.findViewById(R.id.ivCartFoodImage);
            tvCartFoodName = itemView.findViewById(R.id.tvCartFoodName);
            tvCartFoodPrice = itemView.findViewById(R.id.tvCartFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrement = itemView.findViewById(R.id.btnIncrement);
            btnDecrement = itemView.findViewById(R.id.btnDecrement);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}