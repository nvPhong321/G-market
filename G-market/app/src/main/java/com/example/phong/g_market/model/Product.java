package com.example.phong.g_market.model;

/**
 * Created by phong on 2/2/2018.
 */

public class Product {
    private String userId, productId;
    private String name, cost, shop;
    private int number;
    private String categories;
    private String images;

    public Product() {

    }

    public Product(String userId, String productId, String name, String cost, String shop, int number, String categories, String images) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.cost = cost;
        this.shop = shop;
        this.number = number;
        this.categories = categories;
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
