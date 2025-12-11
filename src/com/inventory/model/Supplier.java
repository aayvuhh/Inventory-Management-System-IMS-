package com.inventory.model;

public class Supplier {

    private int id;
    private String name;
    private String contactEmail;
    private String phone;

    public Supplier(int id, String name, String contactEmail, String phone) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getContactEmail() { return contactEmail; }
    public String getPhone() { return phone; }
}
