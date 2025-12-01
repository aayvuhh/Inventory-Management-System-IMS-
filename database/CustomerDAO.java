package com.inventory.database;

import com.inventory.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO (Data Access Object) handles all database operations for Customer entities.
 * This class provides methods for creating, reading, updating, and managing customers.
 *
 * @author IMS Team
 * @version 2.0
 */
public class CustomerDAO {

    private final Connection connection;

    /**
     * Constructs a CustomerDAO with the database connection.
     */
    public CustomerDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new customer into the database.
     *
     * @param customer The Customer object to insert
     * @return The generated customer ID, or -1 if insertion failed
     */
    public int insertCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_name, email_address, phone_number) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The unique customer identifier
     * @return The Customer object if found, null otherwise
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractCustomerFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving customer: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return List of all Customer objects
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                customerList.add(extractCustomerFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all customers: " + e.getMessage());
        }

        return customerList;
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The Customer object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET customer_name = ?, email_address = ?, phone_number = ? WHERE customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setInt(4, customer.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * @param customerId The customer identifier
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts a Customer object from a ResultSet row.
     *
     * @param resultSet The ResultSet positioned at a customer row
     * @return The extracted Customer object
     * @throws SQLException if database access error occurs
     */
    private Customer extractCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        int customerId = resultSet.getInt("customer_id");
        String customerName = resultSet.getString("customer_name");
        String emailAddress = resultSet.getString("email_address");
        String phoneNumber = resultSet.getString("phone_number");

        return new Customer(customerId, customerName, emailAddress, phoneNumber);
    }
}
