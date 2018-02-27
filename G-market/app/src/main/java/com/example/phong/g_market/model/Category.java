package com.example.phong.g_market.model;

/**
 * Created by phong on 2/2/2018.
 */

public class Category {

    private String images;
    private String name;

    public Category(){

    }

    public Category(String images, String name) {
        this.images = images;
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
