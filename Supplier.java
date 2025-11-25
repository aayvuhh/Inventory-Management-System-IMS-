package com.inventory.model;

//Suppliers
public class Supplier {
    private int id;
    private String name;
    private String contactEmail;
    private String phone;

    //constructors
    public Supplier(int id, String name, String contactEmail, String phone) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    //getters setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
