package com.inventory.model;

/**
 * Supplier represents a vendor or supplier from whom the business purchases inventory.
 * It maintains contact information and identification for supplier management and
 * purchase order tracking.
 *
 * @author IMS Team
 * @version 2.0
 */
public class Supplier {
    /** Unique identifier for the supplier */
    private int id;

    /** Company or supplier name */
    private String name;

    /** Email address for supplier contact */
    private String contactEmail;

    /** Phone number for supplier contact */
    private String phone;

    /**
     * Constructs a new Supplier with complete contact information.
     *
     * @param id Unique supplier identifier
     * @param name Supplier company name
     * @param contactEmail Email address for communication
     * @param phone Phone number for communication
     */
    public Supplier(int id, String name, String contactEmail, String phone) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    /**
     * Gets the unique identifier of this supplier.
     *
     * @return The supplier ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this supplier.
     *
     * @return The supplier company name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the contact email of this supplier.
     *
     * @return The supplier's email address
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Gets the phone number of this supplier.
     *
     * @return The supplier's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns a string representation of this supplier.
     *
     * @return A formatted string containing supplier name and ID
     */
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
