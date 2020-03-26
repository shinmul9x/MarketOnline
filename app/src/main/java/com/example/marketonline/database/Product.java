package com.example.marketonline.database;

import java.util.ArrayList;

public class Product {
    private int id;
    private String productName;
    private ArrayList<String> images;
    private int cost;
    private String status;
    private int userId;
    private String description;
    private String address;

    public Product(int id, String productName, ArrayList<String> images, int cost, String status, int userId, String description, String address) {
        this.id = id;
        this.productName = productName;
        this.images = images;
        this.cost = cost;
        this.status = status;
        this.userId = userId;
        this.description = description;
        this.address = address;
    }

    public Product(String productName, ArrayList<String> images, int cost, String status, int userId, String description, String address) {
        this.productName = productName;
        this.images = images;
        this.cost = cost;
        this.status = status;
        this.userId = userId;
        this.description = description;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        if (images.size() == 0) return "";
        return images.get(0);
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImage(ArrayList<String> images) {
        this.images = images;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
