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

import java.util.List;


 // Adapter for horizontal category RecyclerView
 // Displays food categories (All, Burger, Wings, Pizza)
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<String> categories;
    private int selectedPosition = 0;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.tvCategoryName.setText(category);

        // Set category image based on name
        int imageRes = getCategoryImage(category);
        holder.ivCategoryImage.setImageResource(imageRes);

        // Highlight selected category
        if (position == selectedPosition) {
            // Selected: Purple Background, White Text
            ((androidx.cardview.widget.CardView) holder.itemView).setCardBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(context, R.color.purple_primary));
            holder.tvCategoryName.setTextColor(
                    androidx.core.content.ContextCompat.getColor(context, R.color.white));
        } else {
            // Unselected: White Background, Dark Text
            ((androidx.cardview.widget.CardView) holder.itemView).setCardBackgroundColor(
                    androidx.core.content.ContextCompat.getColor(context, R.color.white));
            holder.tvCategoryName.setTextColor(
                    androidx.core.content.ContextCompat.getColor(context, R.color.text_primary));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onCategoryClick(category, selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

     // Get category image resource
    private int getCategoryImage(String category) {
        switch (category) {
            case "Burger":
                return R.drawable.cat_burger;
            case "Wings":
                return R.drawable.cat_wings;
            case "Pizza":
                return R.drawable.cat_pizza;
            case "All":
                return R.drawable.cat_all;
            default:
                return android.R.drawable.ic_menu_gallery;
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}