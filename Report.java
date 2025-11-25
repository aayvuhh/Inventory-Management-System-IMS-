package com.inventory.model;

import java.time.LocalDateTime;


public class Report {
    private int id;
    private InventoryManager createdBy;
    private ReportType type;
    private String content;
    private LocalDateTime createdAt;

    //conconcon
    public Report(int id, InventoryManager createdBy, ReportType type, String content) {
        this.id = id;
        this.createdBy = createdBy;
        this.type = type;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public InventoryManager getCreatedBy() {
        return createdBy;
    }

    public ReportType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
