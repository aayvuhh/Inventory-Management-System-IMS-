package com.inventory.model;

import java.time.LocalDate;

/**
 * Product extends Item to represent a physical product in the inventory system.
 * In addition to basic item properties, it tracks stock levels, expiry dates,
 * and reorder thresholds for inventory management.
 *
 * @author IMS Team
 * @version 2.0
 */
public class Product extends Item {
    /** Current quantity of this product in stock */
    private int stockLevel;

    /** Expiration date for perishable products (null for non-perishable items) */
    private LocalDate expiryDate;

    /** Minimum stock level that triggers a reorder alert */
    private int reorderLevel;

    /**
     * Constructs a new Product with complete inventory information.
     *
     * @param id Unique product identifier
     * @param name Product name
     * @param category Product category (e.g., Electronics, Food, Stationery)
     * @param unitPrice Price per unit in dollars
     * @param stockLevel Current quantity in stock
     * @param expiryDate Expiration date (null if non-perishable)
     * @param reorderLevel Minimum stock threshold before reorder
     */
    public Product(String id, String name, String category, double unitPrice,
                   int stockLevel, LocalDate expiryDate, int reorderLevel) {
        super(id, name, category, unitPrice);
        this.stockLevel = stockLevel;
        this.expiryDate = expiryDate;
        this.reorderLevel = reorderLevel;
    }

    /**
     * Gets the current stock level of this product.
     *
     * @return The quantity currently in stock
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Sets the stock level of this product.
     *
     * @param stockLevel The new stock quantity
     */
    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    /**
     * Gets the expiry date of this product.
     *
     * @return The expiration date, or null if non-perishable
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of this product.
     *
     * @param expiryDate The new expiration date
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Gets the reorder level for this product.
     *
     * @return The minimum stock threshold
     */
    public int getReorderLevel() {
        return reorderLevel;
    }

    /**
     * Sets the reorder level for this product.
     *
     * @param reorderLevel The new minimum stock threshold
     */
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    /**
     * Checks if this product has low stock (at or below reorder level).
     *
     * @return true if stock level is at or below reorder level, false otherwise
     */
    public boolean isLowStock() {
        return stockLevel <= reorderLevel;
    }
}
