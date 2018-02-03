package com.example.phong.g_market.model;

import android.graphics.Bitmap;

/**
 * Created by phong on 2/2/2018.
 */

public class Category {

    private Bitmap images;
    private String name;

    public Category(Bitmap images, String name) {
        this.images = images;
        this.name = name;
    }

    public Bitmap getImages() {
        return images;
    }

    public void setImages(Bitmap images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
