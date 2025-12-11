package com.inventory.model;

import java.time.LocalDateTime;

public class StockRequest {

    private int id;
    private Product product;
    private int quantity;
    private double costPrice;
    private double salePrice;
    private double expectedRevenue;
    private double expectedProfit;
    private Account requestedBy;
    private Account approvedBy;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime requestedAt;
    private LocalDateTime decidedAt;

    public StockRequest(int id,
                        Product product,
                        int quantity,
                        double costPrice,
                        double salePrice,
                        double expectedRevenue,
                        double expectedProfit,
                        Account requestedBy,
                        String status,
                        LocalDateTime requestedAt) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.expectedRevenue = expectedRevenue;
        this.expectedProfit = expectedProfit;
        this.requestedBy = requestedBy;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    public int getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getCostPrice() { return costPrice; }
    public double getSalePrice() { return salePrice; }
    public double getExpectedRevenue() { return expectedRevenue; }
    public double getExpectedProfit() { return expectedProfit; }
    public Account getRequestedBy() { return requestedBy; }
    public Account getApprovedBy() { return approvedBy; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public LocalDateTime getDecidedAt() { return decidedAt; }

    public void approve(Account manager) {
        this.status = "APPROVED";
        this.approvedBy = manager;
        this.decidedAt = LocalDateTime.now();
    }

    public void reject(Account manager) {
        this.status = "REJECTED";
        this.approvedBy = manager;
        this.decidedAt = LocalDateTime.now();
    }
}
