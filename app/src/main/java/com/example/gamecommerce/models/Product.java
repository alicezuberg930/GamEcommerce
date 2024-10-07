package com.example.gamecommerce.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private int price;
    private String description;
    private float rating;
    private int discount;
    private String type;
    private String imageUrl;
    public Product(String name, int price, String description, int rating, int discount, String type, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.discount = discount;
        this.type = type;
        this.imageUrl = imageUrl;
   }

    public Product() {}
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public int getDiscount() {
        return discount;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
