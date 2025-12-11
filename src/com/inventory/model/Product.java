package com.inventory.model;

import java.time.LocalDate;

public class Product {

    private String id;
    private String name;
    private String category;
    private double unitPrice;
    private int stockLevel;
    private LocalDate expiryDate;
    private int reorderLevel;

    public Product(String id, String name, String category,
                   double unitPrice, int stockLevel,
                   LocalDate expiryDate, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.stockLevel = stockLevel;
        this.expiryDate = expiryDate;
        this.reorderLevel = reorderLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getUnitPrice() { return unitPrice; }
    public int getStockLevel() { return stockLevel; }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
    public int getReorderLevel() { return reorderLevel; }

    public boolean isLowStock() {
        return stockLevel <= reorderLevel;
    }
}
