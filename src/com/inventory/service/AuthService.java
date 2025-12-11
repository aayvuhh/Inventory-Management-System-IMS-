package com.inventory.service;

import com.inventory.db.DatabaseHelper;
import com.inventory.model.Account;
import com.inventory.model.PasswordResetRequest;
import com.inventory.model.UserRole;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    public Account registerEmployee(String firstName, String lastName,
                                    String email, String phone, String password) throws Exception {

        String normalizedEmail = email.trim().toLowerCase();

        if (!normalizedEmail.endsWith("@psu.edu")) {
            throw new IllegalArgumentException("Email must be a @psu.edu address.");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        String digitsOnly = phone.replaceAll("\\D", "");
        if (digitsOnly.length() != 10) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }

        String sql = "INSERT INTO Users (first_name, last_name, email, phone, password, role) " +
                "VALUES (?,?,?,?,?,?)";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, normalizedEmail);
            ps.setString(4, digitsOnly);
            ps.setString(5, password);
            ps.setString(6, "EMPLOYEE");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new Account(id, firstName, lastName,
                            normalizedEmail, digitsOnly, password, UserRole.EMPLOYEE);
                }
            }
        }
        throw new Exception("Could not create account.");
    }

    public Account login(String email, String password) throws Exception {
        String normalizedEmail = email.trim().toLowerCase();

        if (!normalizedEmail.endsWith("@psu.edu")) {
            throw new IllegalArgumentException("Login requires a @psu.edu email address.");
        }

        String sql = "SELECT id, first_name, last_name, email, phone, password, role " +
                "FROM Users WHERE email = ?";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, normalizedEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("No account found with that email.");
                }
                String storedPass = rs.getString("password");
                if (!storedPass.equals(password)) {
                    throw new IllegalArgumentException("Incorrect password.");
                }
                int id = rs.getInt("id");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String phone = rs.getString("phone");
                String roleStr = rs.getString("role");
                UserRole role = UserRole.valueOf(roleStr.toUpperCase());

                return new Account(id, first, last, normalizedEmail, phone, storedPass, role);
            }
        }
    }

    public void requestPasswordReset(String email) throws Exception {
        Integer userId = null;
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id FROM Users WHERE email = ?")) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                }
            }
        }
        if (userId == null) {
            throw new IllegalArgumentException("No account found with that email.");
        }

        String sql = "INSERT INTO PasswordResetRequests (user_id, email, status, requested_at) " +
                "VALUES (?,?,?,?)";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, email.toLowerCase());
            ps.setString(3, "PENDING");
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

    public List<PasswordResetRequest> getPendingResetRequests() throws SQLException {
        List<PasswordResetRequest> list = new ArrayList<>();
        String sql = "SELECT id, user_id, email, status, requested_at " +
                "FROM PasswordResetRequests WHERE status = 'PENDING'";

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PasswordResetRequest(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getTimestamp("requested_at").toLocalDateTime()
                ));
            }
        }
        return list;
    }

    public String approveReset(int requestId) throws SQLException {
        int userId = -1;

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT user_id FROM PasswordResetRequests WHERE id = ?")) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                }
            }
        }
        if (userId == -1) {
            throw new IllegalArgumentException("Request not found.");
        }

        String newPassword = generateRandomPassword(10);

        try (Connection con = DatabaseHelper.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE Users SET password = ? WHERE id = ?")) {
                ps.setString(1, newPassword);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE PasswordResetRequests SET status = 'APPROVED' WHERE id = ?")) {
                ps.setInt(1, requestId);
                ps.executeUpdate();
            }
        }

        return newPassword;
    }

    public void rejectReset(int requestId) throws SQLException {
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE PasswordResetRequests SET status = 'REJECTED' WHERE id = ?")) {
            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }

    public void changePassword(int accountId, String oldPassword, String newPassword) throws Exception {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters.");
        }

        String sql = "SELECT password FROM Users WHERE id = ?";
        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("User not found.");
                }
                String cur = rs.getString("password");
                if (!cur.equals(oldPassword)) {
                    throw new IllegalArgumentException("Old password is incorrect.");
                }
            }
        }

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE Users SET password = ? WHERE id = ?")) {
            ps.setString(1, newPassword);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
