package com.inventory.model;

/**
 * Item is the base class for all inventory items in the system.
 * It contains fundamental properties shared by all inventory items including
 * identification, naming, categorization, and pricing information.
 *
 * @author IMS Team
 * @version 2.0
 */
public class Item {
    /** Unique identifier for the item */
    protected String id;

    /** Name of the item */
    protected String name;

    /** Category or classification of the item (e.g., Electronics, Food, Office Supplies) */
    protected String category;

    /** Unit price of the item in dollars */
    protected double unitPrice;

    /**
     * Constructs a new Item with the specified properties.
     *
     * @param id Unique identifier for the item
     * @param name Name of the item
     * @param category Category classification
     * @param unitPrice Price per unit in dollars
     */
    public Item(String id, String name, String category, double unitPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the unique identifier of this item.
     *
     * @return The item ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of this item.
     *
     * @return The item name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the category of this item.
     *
     * @return The item category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the unit price of this item.
     *
     * @return The unit price in dollars
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of this item.
     *
     * @param unitPrice The new unit price in dollars
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
