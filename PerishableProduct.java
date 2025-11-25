package com.inventory.model;

import java.time.LocalDate;

//child class
public class PerishableProduct extends Product {
    public PerishableProduct(String id, String name, String category, double unitPrice,
                             int stockLevel, LocalDate expiryDate, int reorderLevel) {
        super(id, name, category, unitPrice, stockLevel, expiryDate, reorderLevel);
    }
}

