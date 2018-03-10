package com.example.phong.g_market.model;

/**
 * Created by phong on 3/6/2018.
 */

public class Cart {

    private String cartId,productId,userId;
    private String imagesProduct,nameProduct,cost,ammount,summary;
    private boolean check,checkClick;

    public Cart(){

    }

    public Cart(String cartId, String productId, String userId, String imagesProduct, String nameProduct, String cost, String ammount, String summary) {
        this.cartId = cartId;
        this.productId = productId;
        this.userId = userId;
        this.imagesProduct = imagesProduct;
        this.nameProduct = nameProduct;
        this.cost = cost;
        this.ammount = ammount;
        this.summary = summary;
        this.check = false;
        this.checkClick = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public boolean isCheckClick() {
        return checkClick;
    }

    public void setCheckClick(boolean checkClick) {
        this.checkClick = checkClick;
    }
}
