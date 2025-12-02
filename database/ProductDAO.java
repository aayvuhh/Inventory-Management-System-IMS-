package com.inventory.database;

import com.inventory.model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO (Data Access Object) handles all database operations for Product entities.
 * This class provides CRUD (Create, Read, Update, Delete) operations and additional
 * query methods for product management.
 *
 * @author IMS Team
 * @version 2.0
 */
public class ProductDAO {

    private final Connection connection;

    /**
     * Constructs a ProductDAO with the database connection.
     */
    public ProductDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The Product object to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO products (product_id, product_name, category, unit_price, " +
                     "stock_level, expiry_date, reorder_level) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getCategory());
            statement.setDouble(4, product.getUnitPrice());
            statement.setInt(5, product.getStockLevel());
            statement.setString(6, product.getExpiryDate() != null ? product.getExpiryDate().toString() : null);
            statement.setInt(7, product.getReorderLevel());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET product_name = ?, category = ?, unit_price = ?, " +
                     "stock_level = ?, expiry_date = ?, reorder_level = ? WHERE product_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getUnitPrice());
            statement.setInt(4, product.getStockLevel());
            statement.setString(5, product.getExpiryDate() != null ? product.getExpiryDate().toString() : null);
            statement.setInt(6, product.getReorderLevel());
            statement.setString(7, product.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The unique product identifier
     * @return The Product object if found, null otherwise
     */
    public Product getProductById(String productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving product: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all Product objects
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                productList.add(extractProductFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all products: " + e.getMessage());
        }

        return productList;
    }

    /**
     * Searches for products by keyword in ID, name, or category.
     *
     * @param keyword The search keyword
     * @return List of matching Product objects
     */
    public List<Product> searchProducts(String keyword) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE product_id LIKE ? OR product_name LIKE ? OR category LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                productList.add(extractProductFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }

        return productList;
    }

    /**
     * Retrieves all products with stock levels at or below their reorder level.
     *
     * @return List of low-stock Product objects
     */
    public List<Product> getLowStockProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock_level <= reorder_level";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                productList.add(extractProductFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving low stock products: " + e.getMessage());
        }

        return productList;
    }

    /**
     * Updates the stock level for a specific product.
     *
     * @param productId The product identifier
     * @param newStockLevel The new stock level
     * @return true if update was successful, false otherwise
     */
    public boolean updateStockLevel(String productId, int newStockLevel) {
        String sql = "UPDATE products SET stock_level = ? WHERE product_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newStockLevel);
            statement.setString(2, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating stock level: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param productId The product identifier
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts a Product object from a ResultSet row.
     *
     * @param resultSet The ResultSet positioned at a product row
     * @return The extracted Product object
     * @throws SQLException if database access error occurs
     */
    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        String productId = resultSet.getString("product_id");
        String productName = resultSet.getString("product_name");
        String category = resultSet.getString("category");
        double unitPrice = resultSet.getDouble("unit_price");
        int stockLevel = resultSet.getInt("stock_level");
        String expiryDateString = resultSet.getString("expiry_date");
        int reorderLevel = resultSet.getInt("reorder_level");

        // Parse expiry date if it exists
        LocalDate expiryDate = (expiryDateString != null && !expiryDateString.isEmpty())
                ? LocalDate.parse(expiryDateString)
                : null;

        return new Product(productId, productName, category, unitPrice, stockLevel, expiryDate, reorderLevel);
    }
}
