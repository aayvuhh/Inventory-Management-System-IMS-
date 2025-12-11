package com.inventory.model;

import java.time.LocalDateTime;

public class PasswordResetRequest {

    private int id;
    private int accountId;
    private String email;
    private String status;
    private LocalDateTime requestedAt;

    public PasswordResetRequest(int id, int accountId, String email,
                                String status, LocalDateTime requestedAt) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    public int getId() { return id; }
    public int getAccountId() { return accountId; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
}
