package com.inventory.model;

import java.time.LocalDate;

public class Sale {

    private int id;
    private Product product;
    private int quantity;
    private double salePrice;
    private double costPrice;
    private LocalDate date;
    private Account soldBy;

    public Sale(int id, Product product, int quantity,
                double salePrice, double costPrice, LocalDate date, Account soldBy) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.costPrice = costPrice;
        this.date = date;
        this.soldBy = soldBy;
    }

    public int getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getSalePrice() { return salePrice; }
    public double getCostPrice() { return costPrice; }
    public LocalDate getDate() { return date; }
    public Account getSoldBy() { return soldBy; }

    public double getRevenue() { return salePrice * quantity; }

    public double getProfit() { return (salePrice - costPrice) * quantity; }
}
