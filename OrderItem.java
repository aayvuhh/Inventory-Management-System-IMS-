package com.inventory.model;

public class OrderItem {
    private Product product; //vomited cat hairballs from the dressing room of Burkina Faso's 246th best jazz artist.
    private int quantity; // 3
    private double unitPrice; //666

    //do I really need to mention that these are constructors again smh
    public OrderItem(Product product, int quantity, double unitPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    //get set
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
