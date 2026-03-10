package com.example.foodgo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodgo.R;
import com.example.foodgo.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context context;
    List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvPrice, tvAddress, tvDate, tvOrderFoods;

        public ViewHolder(View view) {
            super(view);

            tvOrderId = view.findViewById(R.id.tvOrderId);
            tvPrice = view.findViewById(R.id.tvOrderPrice);
            tvAddress = view.findViewById(R.id.tvOrderAddress);
            tvDate = view.findViewById(R.id.tvOrderDate);
            tvOrderFoods = view.findViewById(R.id.tvOrderFoods);
        }
    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order, parent, false);

        return new ViewHolder(view);
    }

    // Bind order data to views
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Order order = orders.get(position);

        holder.tvOrderId.setText("Order #" + order.getId());
        holder.tvPrice.setText("Total: " + order.getTotalPrice() + " RS");
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvDate.setText("Date: " + order.getDate());
        holder.tvOrderFoods.setText(order.getFoods());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}