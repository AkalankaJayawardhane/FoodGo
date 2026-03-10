package com.example.foodgo.models;

import com.example.foodgo.models.FoodItem;

// Cart Item - Represents an item in the user's shopping cart
public class CartItem {
    private int id;
    private int userId;
    private FoodItem foodItem;
    private int quantity;

    // Constructors
    public CartItem() {
    }

    public CartItem(int userId, FoodItem foodItem, int quantity) {
        this.userId = userId;
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public CartItem(int id, int userId, FoodItem foodItem, int quantity) {
        this.id = id;
        this.userId = userId;
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Other methods
    public int getSubtotal() {
        return foodItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", foodItem=" + foodItem.getName() +
                ", quantity=" + quantity +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}