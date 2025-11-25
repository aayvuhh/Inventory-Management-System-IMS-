package com.inventory.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
    private int id;
    private Supplier supplier;
    private Customer customer; // optional, for customer-specific orders
    private InventoryManager createdBy;
    private LocalDate createdDate;
    private OrderStatus status;
    private List<OrderItem> items = new ArrayList<>();

    //constructor
    public PurchaseOrder(int id, Supplier supplier, InventoryManager createdBy) {
        this.id = id;
        this.supplier = supplier;
        this.createdBy = createdBy;
        this.createdDate = LocalDate.now();
        this.status = OrderStatus.CREATED;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public InventoryManager getCreatedBy() {
        return createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(OrderItem::getLineTotal)
                .sum();
    }
}
