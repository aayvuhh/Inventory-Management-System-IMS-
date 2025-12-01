package com.inventory.database;

import com.inventory.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SupplierDAO (Data Access Object) handles all database operations for Supplier entities.
 * This class provides methods for creating, reading, updating, and managing suppliers.
 *
 * @author IMS Team
 * @version 2.0
 */
public class SupplierDAO {

    private final Connection connection;

    /**
     * Constructs a SupplierDAO with the database connection.
     */
    public SupplierDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new supplier into the database.
     *
     * @param supplier The Supplier object to insert
     * @return The generated supplier ID, or -1 if insertion failed
     */
    public int insertSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (supplier_name, contact_email, phone_number) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactEmail());
            statement.setString(3, supplier.getPhone());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting supplier: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Retrieves a supplier by their ID.
     *
     * @param supplierId The unique supplier identifier
     * @return The Supplier object if found, null otherwise
     */
    public Supplier getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractSupplierFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving supplier: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all suppliers from the database.
     *
     * @return List of all Supplier objects
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                supplierList.add(extractSupplierFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all suppliers: " + e.getMessage());
        }

        return supplierList;
    }

    /**
     * Updates an existing supplier in the database.
     *
     * @param supplier The Supplier object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET supplier_name = ?, contact_email = ?, phone_number = ? WHERE supplier_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactEmail());
            statement.setString(3, supplier.getPhone());
            statement.setInt(4, supplier.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a supplier from the database.
     *
     * @param supplierId The supplier identifier
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts a Supplier object from a ResultSet row.
     *
     * @param resultSet The ResultSet positioned at a supplier row
     * @return The extracted Supplier object
     * @throws SQLException if database access error occurs
     */
    private Supplier extractSupplierFromResultSet(ResultSet resultSet) throws SQLException {
        int supplierId = resultSet.getInt("supplier_id");
        String supplierName = resultSet.getString("supplier_name");
        String contactEmail = resultSet.getString("contact_email");
        String phoneNumber = resultSet.getString("phone_number");

        return new Supplier(supplierId, supplierName, contactEmail, phoneNumber);
    }
}
