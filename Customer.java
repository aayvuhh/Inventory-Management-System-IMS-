package com.inventory.model;

/**
 * Customer represents a client or customer who purchases products from the inventory.
 * It maintains customer identification and contact information for sales tracking
 * and customer relationship management.
 *
 * @author IMS Team
 * @version 2.0
 */
public class Customer {
    /** Unique identifier for the customer */
    private int id;

    /** Customer's full name */
    private String name;

    /** Customer's email address */
    private String email;

    /** Customer's phone number */
    private String phone;

    /**
     * Constructs a new Customer with complete contact information.
     *
     * @param id Unique customer identifier
     * @param name Customer's full name
     * @param email Customer's email address
     * @param phone Customer's phone number
     */
    public Customer(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Gets the unique identifier of this customer.
     *
     * @return The customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this customer.
     *
     * @return The customer's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email address of this customer.
     *
     * @return The customer's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number of this customer.
     *
     * @return The customer's phone number
     */
    public String getPhone() {
        return phone;
    }
}
