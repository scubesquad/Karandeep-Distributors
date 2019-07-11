package com.scube.karandeepdistributors.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Order model class for maintaining order details
 */
public class Order implements Serializable, Comparable<Order>{
    private String orderId, orderPrice, orderDate, product_id, productName, brandId, brandName, categoryId,
            productImage, productCode, productVolume, productPrice, quantity,placeOrderId,placeOrderquantity;
    ArrayList<Order> orderArrayList = new ArrayList<>();

    public String getPlaceOrderId() {
        return placeOrderId;
    }

    public void setPlaceOrderId(String placeOrderId) {
        this.placeOrderId = placeOrderId;
    }

    public String getPlaceOrderquantity() {
        return placeOrderquantity;
    }

    public void setPlaceOrderquantity(String placeOrderquantity) {
        this.placeOrderquantity = placeOrderquantity;
    }

    public ArrayList<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList1) {
        this.orderArrayList = orderArrayList1;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(String productVolume) {
        this.productVolume = productVolume;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public String setQuantity(String quantity) {
        this.quantity = quantity;
        return quantity;
    }

    @Override
    public int compareTo(@NonNull Order o) {
            return 0;
    }
}
