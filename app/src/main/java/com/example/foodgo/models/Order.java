package com.example.foodgo.models;

public class Order {

    private int id;
    private int userId;
    private int totalPrice;
    private String address;
    private String date;
    private String foods;

    // Constructors
    public Order(int id, int userId, int totalPrice, String address, String date, String foods) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.address = address;
        this.date = date;
        this.foods = foods;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }
}