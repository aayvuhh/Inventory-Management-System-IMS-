package com.inventory.model;

import java.time.LocalDate;

public class Product extends Item {
    private int stockLevel;
    private LocalDate expiryDate; // can be null if not perishable
    private int reorderLevel;

    //constructor
    public Product(String id, String name, String category, double unitPrice,
                   int stockLevel, LocalDate expiryDate, int reorderLevel) {
        super(id, name, category, unitPrice);
        this.stockLevel = stockLevel;
        this.expiryDate = expiryDate;
        this.reorderLevel = reorderLevel;
    }

    //getters and setters
    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isLowStock() {
        return stockLevel <= reorderLevel;
    }
}
