package com.example.foodgo.models;
public class FoodItem {
    private int id;
    private String name;
    private String description;
    private int price;
    private String category;
    private int imageResource;

    // Constructors
    public FoodItem() {
    }

    public FoodItem(String name, String description, int price, String category, int imageResource) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
    }

    public FoodItem(int id, String name, String description, int price, String category, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageResource = imageResource;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
