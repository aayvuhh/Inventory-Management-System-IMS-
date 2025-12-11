package com.inventory.db;

import com.inventory.service.InventoryService;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:derby:InventoryDB;create=true";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeAndLoadSampleData(InventoryService service) {
        try (Connection con = getConnection()) {
            createTablesIfNeeded(con);
            insertSampleDataIfEmpty(con);
            loadDataIntoService(con, service);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNeeded(Connection con) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();
        try (Statement st = con.createStatement()) {

            if (!tableExists(meta, "PRODUCTS")) {
                st.executeUpdate(
                        "CREATE TABLE Products (" +
                                "id VARCHAR(20) PRIMARY KEY, " +
                                "name VARCHAR(100), " +
                                "category VARCHAR(50), " +
                                "price DOUBLE, " +
                                "stock INT, " +
                                "reorder_level INT" +
                                ")"
                );
            }

            if (!tableExists(meta, "SUPPLIERS")) {
                st.executeUpdate(
                        "CREATE TABLE Suppliers (" +
                                "id INT PRIMARY KEY, " +
                                "name VARCHAR(100), " +
                                "email VARCHAR(100), " +
                                "phone VARCHAR(30)" +
                                ")"
                );
            }

            if (!tableExists(meta, "CUSTOMERS")) {
                st.executeUpdate(
                        "CREATE TABLE Customers (" +
                                "id INT PRIMARY KEY, " +
                                "name VARCHAR(100), " +
                                "email VARCHAR(100), " +
                                "phone VARCHAR(30)" +
                                ")"
                );
            }

            if (!tableExists(meta, "PURCHASEORDERS")) {
                st.executeUpdate(
                        "CREATE TABLE PurchaseOrders (" +
                                "id INT PRIMARY KEY, " +
                                "supplier_id INT, " +
                                "created_date DATE, " +
                                "status VARCHAR(20)" +
                                ")"
                );
            }

            if (!tableExists(meta, "ORDERITEMS")) {
                st.executeUpdate(
                        "CREATE TABLE OrderItems (" +
                                "id INT, " +
                                "purchase_order_id INT, " +
                                "product_id VARCHAR(20), " +
                                "quantity INT, " +
                                "unit_price DOUBLE" +
                                ")"
                );
            }

            if (!tableExists(meta, "USERS")) {
                st.executeUpdate(
                        "CREATE TABLE Users (" +
                                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                                "first_name VARCHAR(50), " +
                                "last_name VARCHAR(50), " +
                                "email VARCHAR(100) UNIQUE, " +
                                "phone VARCHAR(30), " +
                                "password VARCHAR(100), " +
                                "role VARCHAR(20)" +
                                ")"
                );
            }

            if (!tableExists(meta, "PASSWORDRESETREQUESTS")) {
                st.executeUpdate(
                        "CREATE TABLE PasswordResetRequests (" +
                                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                                "user_id INT, " +
                                "email VARCHAR(100), " +
                                "status VARCHAR(20), " +
                                "requested_at TIMESTAMP" +
                                ")"
                );
            }

            if (!tableExists(meta, "SALES")) {
                st.executeUpdate(
                        "CREATE TABLE Sales (" +
                                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                                "product_id VARCHAR(20), " +
                                "quantity INT, " +
                                "sale_price DOUBLE, " +
                                "cost_price DOUBLE, " +
                                "sale_date DATE" +
                                ")"
                );
            }
        }
    }

    private static boolean tableExists(DatabaseMetaData meta, String name) throws SQLException {
        try (ResultSet rs = meta.getTables(null, null, name.toUpperCase(), null)) {
            return rs.next();
        }
    }

    private static void insertSampleDataIfEmpty(Connection con) throws SQLException {
        if (isTableEmpty(con, "PRODUCTS")) insertSampleProducts(con);
        if (isTableEmpty(con, "SUPPLIERS")) insertSampleSuppliers(con);
        if (isTableEmpty(con, "CUSTOMERS")) insertSampleCustomers(con);
        if (isTableEmpty(con, "PURCHASEORDERS")) insertSamplePurchaseOrders(con);
        if (isTableEmpty(con, "ORDERITEMS")) insertSampleOrderItems(con);
        if (isTableEmpty(con, "USERS")) insertSampleUsers(con);
    }

    private static boolean isTableEmpty(Connection con, String table) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private static void insertSampleProducts(Connection con) throws SQLException {
        String sql = "INSERT INTO Products (id, name, category, price, stock, reorder_level) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Object[][] data = {
                    {"P001", "Notebook", "Stationery", 2.99, 50, 10},
                    {"P002", "Ball Pen", "Stationery", 0.99, 200, 30},
                    {"P003", "USB Drive 32GB", "Electronics", 12.50, 20, 5},
                    {"P004", "24\" LED Monitor", "Electronics", 129.99, 15, 3},
                    {"P005", "Mechanical Keyboard", "Electronics", 59.99, 40, 8},
                    {"P006", "Wireless Mouse", "Electronics", 24.99, 60, 10},
                    {"P007", "Ground Coffee 1kg", "Grocery", 15.49, 35, 7},
                    {"P008", "Organic Milk 1L", "Grocery", 3.49, 80, 20},
                    {"P009", "Potato Chips Family Pack", "Grocery", 4.29, 120, 25},
                    {"P010", "Anti-Dandruff Shampoo 500ml", "Personal Care", 8.99, 45, 10}
            };
            for (Object[] row : data) {
                ps.setString(1, (String) row[0]);
                ps.setString(2, (String) row[1]);
                ps.setString(3, (String) row[2]);
                ps.setDouble(4, (Double) row[3]);
                ps.setInt(5, (Integer) row[4]);
                ps.setInt(6, (Integer) row[5]);
                ps.executeUpdate();
            }
        }
    }

    private static void insertSampleSuppliers(Connection con) throws SQLException {
        String sql = "INSERT INTO Suppliers (id, name, email, phone) VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Object[][] data = {
                    {1, "Global Supplies", "contact@global.com", "111-222-3333"},
                    {2, "Tech Warehouse", "sales@techwh.com", "999-111-2222"},
                    {3, "FreshGrocer Distributors", "orders@freshgrocer.com", "800-555-0101"},
                    {4, "CarePlus Manufacturers", "support@careplus.com", "800-555-0202"}
            };
            for (Object[] row : data) {
                ps.setInt(1, (Integer) row[0]);
                ps.setString(2, (String) row[1]);
                ps.setString(3, (String) row[2]);
                ps.setString(4, (String) row[3]);
                ps.executeUpdate();
            }
        }
    }

    private static void insertSampleCustomers(Connection con) throws SQLException {
        String sql = "INSERT INTO Customers (id, name, email, phone) VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Object[][] data = {
                    {1, "John Doe", "john@example.com", "555-1234"},
                    {2, "Jane Smith", "jane@example.com", "555-5678"},
                    {3, "Ali Khan", "ali.khan@example.com", "555-9012"},
                    {4, "Maria Garcia", "maria.garcia@example.com", "555-3456"},
                    {5, "Emily Chen", "emily.chen@example.com", "555-7890"}
            };
            for (Object[] row : data) {
                ps.setInt(1, (Integer) row[0]);
                ps.setString(2, (String) row[1]);
                ps.setString(3, (String) row[2]);
                ps.setString(4, (String) row[3]);
                ps.executeUpdate();
            }
        }
    }

    private static void insertSamplePurchaseOrders(Connection con) throws SQLException {
        String sql = "INSERT INTO PurchaseOrders (id, supplier_id, created_date, status) VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Object[][] data = {
                    {1, 1, LocalDate.of(2024, 10, 1), "RECEIVED"},
                    {2, 2, LocalDate.of(2024, 10, 5), "RECEIVED"},
                    {3, 3, LocalDate.of(2024, 10, 10), "CREATED"}
            };
            for (Object[] row : data) {
                ps.setInt(1, (Integer) row[0]);
                ps.setInt(2, (Integer) row[1]);
                ps.setDate(3, Date.valueOf((LocalDate) row[2]));
                ps.setString(4, (String) row[3]);
                ps.executeUpdate();
            }
        }
    }

    private static void insertSampleOrderItems(Connection con) throws SQLException {
        String sql = "INSERT INTO OrderItems (id, purchase_order_id, product_id, quantity, unit_price) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int id = 1;
            Object[][] data = {
                    {id++, 1, "P001", 100, 2.50},
                    {id++, 1, "P002", 500, 0.80},
                    {id++, 2, "P003", 50, 11.00},
                    {id++, 2, "P004", 20, 120.00},
                    {id++, 3, "P007", 60, 14.00},
                    {id++, 3, "P008", 120, 3.00}
            };
            for (Object[] row : data) {
                ps.setInt(1, (Integer) row[0]);
                ps.setInt(2, (Integer) row[1]);
                ps.setString(3, (String) row[2]);
                ps.setInt(4, (Integer) row[3]);
                ps.setDouble(5, (Double) row[4]);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Sample default users:
     *  - sarim@psu.edu (MANAGER)
     *  - eva@psu.edu (MANAGER)
     *  - akshit@psu.edu (EMPLOYEE)
     *  All passwords: admin123
     */
    private static void insertSampleUsers(Connection con) throws SQLException {
        String sql = "INSERT INTO Users (first_name, last_name, email, phone, password, role) " +
                "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Manager 1 — Sarim
            ps.setString(1, "Sarim");
            ps.setString(2, "Raza");
            ps.setString(3, "sarim@psu.edu");
            ps.setString(4, "8145551234");
            ps.setString(5, "admin123");
            ps.setString(6, "MANAGER");
            ps.executeUpdate();

            // Manager 2 — Eva
            ps.setString(1, "Eva");
            ps.setString(2, "Manager");
            ps.setString(3, "eva@psu.edu");
            ps.setString(4, "8145555678");
            ps.setString(5, "admin123");
            ps.setString(6, "MANAGER");
            ps.executeUpdate();

            // Employee — Akshit
            ps.setString(1, "Akshit");
            ps.setString(2, "Nayyar");
            ps.setString(3, "akshit@psu.edu");
            ps.setString(4, "8145557890");
            ps.setString(5, "admin123");
            ps.setString(6, "EMPLOYEE");
            ps.executeUpdate();
        }
    }

    private static void loadDataIntoService(Connection con, InventoryService service) throws SQLException {
        loadProducts(con, service);
        loadSuppliers(con, service);
        loadCustomers(con, service);
        loadPurchaseOrders(con, service);
        loadOrderItems(con, service);
        loadSales(con, service);
    }

    private static void loadProducts(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT id, name, category, price, stock, reorder_level FROM Products";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addItemFromDatabase(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        null,
                        rs.getInt("reorder_level")
                );
            }
        }
    }

    private static void loadSuppliers(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT id, name, email, phone FROM Suppliers";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addSupplierFromDatabase(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        }
    }

    private static void loadCustomers(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT id, name, email, phone FROM Customers";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addCustomerFromDatabase(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        }
    }

    private static void loadPurchaseOrders(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT id, supplier_id, created_date, status FROM PurchaseOrders";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addPurchaseOrderFromDatabase(
                        rs.getInt("id"),
                        rs.getInt("supplier_id"),
                        rs.getDate("created_date").toLocalDate(),
                        rs.getString("status")
                );
            }
        }
    }

    private static void loadOrderItems(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT purchase_order_id, product_id, quantity, unit_price FROM OrderItems";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addOrderItemFromDatabase(
                        rs.getInt("purchase_order_id"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                );
            }
        }
    }

    private static void loadSales(Connection con, InventoryService service) throws SQLException {
        String sql = "SELECT id, product_id, quantity, sale_price, cost_price, sale_date FROM Sales";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                service.addSaleFromDatabase(
                        rs.getInt("id"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("sale_price"),
                        rs.getDouble("cost_price"),
                        rs.getDate("sale_date").toLocalDate()
                );
            }
        }
    }

    public static void insertSale(String productId, int qty,
                                  double salePrice, double costPrice, LocalDate date)
            throws SQLException {
        String sql = "INSERT INTO Sales (product_id, quantity, sale_price, cost_price, sale_date) " +
                "VALUES (?,?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setInt(2, qty);
            ps.setDouble(3, salePrice);
            ps.setDouble(4, costPrice);
            ps.setDate(5, Date.valueOf(date));
            ps.executeUpdate();
        }
    }
}
