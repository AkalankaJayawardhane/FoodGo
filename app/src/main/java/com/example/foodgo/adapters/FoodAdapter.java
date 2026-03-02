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
import com.example.foodgo.models.FoodItem;

import java.util.List;

import com.bumptech.glide.Glide;

/**
 * Adapter for food items grid RecyclerView
 * Displays food items with image, name, price, and add button
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<FoodItem> foodItems;
    private OnFoodClickListener listener;

    public interface OnFoodClickListener {
        void onFoodClick(FoodItem foodItem);

        void onAddClick(FoodItem foodItem);
    }

    public FoodAdapter(Context context, List<FoodItem> foodItems, OnFoodClickListener listener) {
        this.context = context;
        this.foodItems = foodItems;
        this.listener = listener;
    }

    public void updateList(List<FoodItem> newList) {
        this.foodItems = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.tvFoodName.setText(foodItem.getName());
        holder.tvFoodPrice.setText(foodItem.getPrice() + " RS");

        // Use Glide to load image efficiently
        Glide.with(context)
                .load(foodItem.getImageResource())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery) // Show while loading
                .into(holder.ivFoodImage);

        // Click on item to view details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFoodClick(foodItem);
                }
            }
        });

        // Click on add button to add to cart
        holder.btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAddClick(foodItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage, btnAddFood;
        TextView tvFoodName, tvFoodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            btnAddFood = itemView.findViewById(R.id.btnAddFood);
        }
    }
}
