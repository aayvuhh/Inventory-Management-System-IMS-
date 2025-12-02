package com.inventory.database;

import java.sql.*;

/**
 * DatabaseManager handles SQLite database connections and schema initialization.
 * This class implements the Singleton pattern to ensure only one database connection
 * is maintained throughout the application lifecycle.
 *
 * @author IMS Team
 * @version 2.0
 */
public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:inventory.db";
    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the database connection and creates tables if they don't exist.
     */
    private DatabaseManager() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            initializeDatabaseSchema();
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Gets the singleton instance of DatabaseManager.
     *
     * @return The singleton DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Gets the active database connection.
     *
     * @return The database Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes the database schema by creating all necessary tables.
     * This method is idempotent - it can be called multiple times safely.
     */
    private void initializeDatabaseSchema() {
        try (Statement statement = connection.createStatement()) {

            // Create Products table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS products (" +
                "    product_id TEXT PRIMARY KEY," +
                "    product_name TEXT NOT NULL," +
                "    category TEXT NOT NULL," +
                "    unit_price REAL NOT NULL," +
                "    stock_level INTEGER NOT NULL," +
                "    expiry_date TEXT," +
                "    reorder_level INTEGER NOT NULL" +
                ")"
            );

            // Create Suppliers table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS suppliers (" +
                "    supplier_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    supplier_name TEXT NOT NULL," +
                "    contact_email TEXT," +
                "    phone_number TEXT" +
                ")"
            );

            // Create Customers table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS customers (" +
                "    customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    customer_name TEXT NOT NULL," +
                "    email_address TEXT," +
                "    phone_number TEXT" +
                ")"
            );

            // Create Purchase Orders table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS purchase_orders (" +
                "    order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    supplier_id INTEGER NOT NULL," +
                "    created_date TEXT NOT NULL," +
                "    order_status TEXT NOT NULL," +
                "    total_amount REAL NOT NULL," +
                "    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)" +
                ")"
            );

            // Create Reports table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS reports (" +
                "    report_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    report_type TEXT NOT NULL," +
                "    report_content TEXT NOT NULL," +
                "    created_at TEXT NOT NULL" +
                ")"
            );

            // Create Sales Transactions table for tracking product issues
            statement.execute(
                "CREATE TABLE IF NOT EXISTS sales_transactions (" +
                "    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    product_id TEXT NOT NULL," +
                "    customer_id INTEGER NOT NULL," +
                "    quantity_sold INTEGER NOT NULL," +
                "    transaction_date TEXT NOT NULL," +
                "    total_price REAL NOT NULL," +
                "    FOREIGN KEY (product_id) REFERENCES products(product_id)," +
                "    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
                ")"
            );

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to initialize database schema: " + e.getMessage());
        }
    }

    /**
     * Closes the database connection.
     * Should be called when the application is shutting down.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
