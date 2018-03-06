package com.example.phong.g_market.model;

/**
 * Created by phong on 3/6/2018.
 */

public class Cart {

    private String cartId,productId;
    private String imagesProduct,nameProduct,cost,ammount,summary;
    private Boolean check;

    public Cart(){

    }

    public Cart(String cartId, String productId, String imagesProduct, String nameProduct, String cost, String ammount, String summary) {
        this.cartId = cartId;
        this.productId = productId;
        this.imagesProduct = imagesProduct;
        this.nameProduct = nameProduct;
        this.cost = cost;
        this.ammount = ammount;
        this.summary = summary;
        this.check = false;
    }

    public String getImagesProduct() {
        return imagesProduct;
    }

    public void setImagesProduct(String imagesProduct) {
        this.imagesProduct = imagesProduct;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
