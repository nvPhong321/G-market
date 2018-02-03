package com.example.phong.g_market.model;

import android.graphics.Bitmap;

/**
 * Created by phong on 2/2/2018.
 */

public class Product {
    private String name, cost, shop;
    private Bitmap images;

    public Product(String name, String cost, String shop, Bitmap images) {
        this.name = name;
        this.cost = cost;
        this.shop = shop;
        this.images = images;
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

    public Bitmap getImages() {
        return images;
    }

    public void setImages(Bitmap images) {
        this.images = images;
    }
}
