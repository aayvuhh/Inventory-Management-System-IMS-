package com.inventory.model;

public class OrderItem {
    private Product product; //
    private int quantity; // 3
    private double unitPrice; //666

    //constructors
    public OrderItem(Product product, int quantity, double unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    //getters and setters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }
}
