package com.inventory.service;

import com.inventory.database.*;
import com.inventory.model.*;

import java.time.LocalDate;
import java.util.*;

/**
 * InventoryService is the main business logic layer for the Inventory Management System.
 * It coordinates all inventory operations including product management, supplier/customer
 * handling, purchase orders, sales transactions, and reporting.
 *
 * This service uses DAO (Data Access Object) classes to persist data in a SQLite database,
 * replacing the previous in-memory storage approach.
 *
 * @author IMS Team
 * @version 2.0
 */
public class InventoryService {

    // Data Access Objects for database operations
    private final ProductDAO productDAO;
    private final SupplierDAO supplierDAO;
    private final CustomerDAO customerDAO;
    private final ReportDAO reportDAO;

    // In-memory storage for purchase orders (can be migrated to database in future)
    private final List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private int nextOrderId = 1;

    // Default inventory manager for system operations
    private final InventoryManager defaultManager =
            new InventoryManager(1, "System Administrator", "admin@inventory-system.com");

    /**
     * Constructs a new InventoryService and initializes all DAO objects.
     * Also initializes the database connection through DatabaseManager.
     */
    public InventoryService() {
        // Initialize database connection
        DatabaseManager.getInstance();

        // Initialize DAOs
        this.productDAO = new ProductDAO();
        this.supplierDAO = new SupplierDAO();
        this.customerDAO = new CustomerDAO();
        this.reportDAO = new ReportDAO();
    }

    // ==================== PRODUCT MANAGEMENT ====================

    /**
     * Adds a new product to the inventory database.
     *
     * @param productId Unique identifier for the product
     * @param productName Name of the product
     * @param category Product category (e.g., Electronics, Food, Stationery)
     * @param unitPrice Price per unit
     * @param stockLevel Current quantity in stock
     * @param expiryDate Expiration date (null if non-perishable)
     * @param reorderLevel Minimum stock level before reorder is needed
     * @return The created Product object, or null if creation failed
     */
    public Product addItem(String productId, String productName, String category, double unitPrice,
                           int stockLevel, LocalDate expiryDate, int reorderLevel) {

        Product product = new Product(productId, productName, category, unitPrice,
                                     stockLevel, expiryDate, reorderLevel);

        // Check if product already exists
        Product existingProduct = productDAO.getProductById(productId);
        if (existingProduct != null) {
            // Update existing product
            productDAO.updateProduct(product);
        } else {
            // Insert new product
            productDAO.insertProduct(product);
        }

        return product;
    }

    /**
     * Updates the stock level of a product by a delta quantity.
     * Positive values increase stock, negative values decrease stock.
     *
     * @param productId The product identifier
     * @param deltaQuantity The change in quantity (positive or negative)
     * @throws IllegalArgumentException if product not found or insufficient stock
     */
    public void updateStock(String productId, int deltaQuantity) {
        Product product = productDAO.getProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }

        int newStockLevel = product.getStockLevel() + deltaQuantity;

        if (newStockLevel < 0) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }

        productDAO.updateStockLevel(productId, newStockLevel);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return List of all products in inventory
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Searches for products by keyword in ID, name, or category.
     *
     * @param keyword Search term
     * @return List of matching products
     */
    public List<Product> searchItems(String keyword) {
        return productDAO.searchProducts(keyword);
    }

    /**
     * Retrieves all products with stock levels at or below their reorder level.
     *
     * @return List of products that need reordering
     */
    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }

    // ==================== SUPPLIER MANAGEMENT ====================

    /**
     * Adds a new supplier to the database.
     *
     * @param supplierName Name of the supplier company
     * @param contactEmail Email address for contact
     * @param phoneNumber Phone number for contact
     * @return The created Supplier object, or null if creation failed
     */
    public Supplier addSupplier(String supplierName, String contactEmail, String phoneNumber) {
        Supplier supplier = new Supplier(0, supplierName, contactEmail, phoneNumber);
        int generatedId = supplierDAO.insertSupplier(supplier);

        if (generatedId > 0) {
            return supplierDAO.getSupplierById(generatedId);
        }

        return null;
    }

    /**
     * Retrieves all suppliers from the database.
     *
     * @return List of all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    /**
     * Retrieves a supplier by their ID.
     *
     * @param supplierId The supplier identifier
     * @return The Supplier object, or null if not found
     */
    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    // ==================== CUSTOMER MANAGEMENT ====================

    /**
     * Adds a new customer to the database.
     *
     * @param customerName Name of the customer
     * @param emailAddress Customer's email address
     * @param phoneNumber Customer's phone number
     * @return The created Customer object, or null if creation failed
     */
    public Customer addCustomer(String customerName, String emailAddress, String phoneNumber) {
        Customer customer = new Customer(0, customerName, emailAddress, phoneNumber);
        int generatedId = customerDAO.insertCustomer(customer);

        if (generatedId > 0) {
            return customerDAO.getCustomerById(generatedId);
        }

        return null;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The customer identifier
     * @return The Customer object, or null if not found
     */
    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    // ==================== PURCHASE ORDERS ====================

    /**
     * Creates a new purchase order for a supplier.
     *
     * @param supplierId The ID of the supplier
     * @return The created PurchaseOrder object
     * @throws IllegalArgumentException if supplier not found
     */
    public PurchaseOrder createPurchaseOrder(int supplierId) {
        Supplier supplier = supplierDAO.getSupplierById(supplierId);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found: " + supplierId);
        }

        PurchaseOrder purchaseOrder = new PurchaseOrder(nextOrderId++, supplier, defaultManager);
        purchaseOrders.add(purchaseOrder);

        return purchaseOrder;
    }

    /**
     * Retrieves all purchase orders.
     *
     * @return List of all purchase orders
     */
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return new ArrayList<>(purchaseOrders);
    }

    // ==================== SALES TRANSACTIONS ====================

    /**
     * Issues products to a customer (records a sale).
     * Decreases the stock level accordingly.
     *
     * @param productId The product being sold
     * @param quantity Number of units sold
     * @param customerId The customer making the purchase
     * @throws IllegalArgumentException if customer not found or insufficient stock
     */
    public void issueProducts(String productId, int quantity, int customerId) {
        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        updateStock(productId, -quantity);
        // Note: Sales transaction recording can be enhanced with SalesTransactionDAO
    }

    /**
     * Processes a product return from a customer.
     * Increases the stock level accordingly.
     *
     * @param productId The product being returned
     * @param quantity Number of units returned
     * @param customerId The customer returning the product
     * @throws IllegalArgumentException if customer not found
     */
    public void returnProducts(String productId, int quantity, int customerId) {
        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }

        updateStock(productId, quantity);
        // Note: Return transaction recording can be enhanced with ReturnTransactionDAO
    }

    // ==================== REPORTING ====================

    /**
     * Generates a comprehensive stock summary report showing all products,
     * their quantities, and prices.
     *
     * @return The generated Report object
     */
    public Report generateStockReport() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("╔════════════════════════════════════════════════════════════╗\n");
        reportContent.append("║            STOCK SUMMARY REPORT                            ║\n");
        reportContent.append("╚════════════════════════════════════════════════════════════╝\n\n");

        List<Product> allProducts = productDAO.getAllProducts();

        if (allProducts.isEmpty()) {
            reportContent.append("No products in inventory.\n");
        } else {
            reportContent.append(String.format("%-15s %-30s %-15s %10s %10s\n",
                "Product ID", "Name", "Category", "Stock", "Price"));
            reportContent.append("─".repeat(85)).append("\n");

            for (Product product : allProducts) {
                reportContent.append(String.format("%-15s %-30s %-15s %10d $%9.2f\n",
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getStockLevel(),
                    product.getUnitPrice()));
            }

            reportContent.append("\nTotal Products: ").append(allProducts.size()).append("\n");
        }

        int reportId = reportDAO.insertReport(ReportType.STOCK_SUMMARY, reportContent.toString());
        return new Report(reportId, defaultManager, ReportType.STOCK_SUMMARY, reportContent.toString());
    }

    /**
     * Generates a low stock alert report showing products that are at or below
     * their reorder level.
     *
     * @return The generated Report object
     */
    public Report generateLowStockReport() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("╔════════════════════════════════════════════════════════════╗\n");
        reportContent.append("║              LOW STOCK ALERT REPORT                        ║\n");
        reportContent.append("╚════════════════════════════════════════════════════════════╝\n\n");

        List<Product> lowStockProducts = productDAO.getLowStockProducts();

        if (lowStockProducts.isEmpty()) {
            reportContent.append("✓ All products are adequately stocked.\n");
        } else {
            reportContent.append(String.format("%-15s %-30s %15s %15s\n",
                "Product ID", "Name", "Current Stock", "Reorder Level"));
            reportContent.append("─".repeat(80)).append("\n");

            for (Product product : lowStockProducts) {
                reportContent.append(String.format("%-15s %-30s %15d %15d ⚠\n",
                    product.getId(),
                    product.getName(),
                    product.getStockLevel(),
                    product.getReorderLevel()));
            }

            reportContent.append("\nProducts Requiring Reorder: ").append(lowStockProducts.size()).append("\n");
        }

        int reportId = reportDAO.insertReport(ReportType.LOW_STOCK, reportContent.toString());
        return new Report(reportId, defaultManager, ReportType.LOW_STOCK, reportContent.toString());
    }

    /**
     * Retrieves all generated reports from the database.
     *
     * @return List of all reports
     */
    public List<Report> getAllReports() {
        return reportDAO.getAllReports(defaultManager);
    }

    // ==================== DEMO DATA ====================

    /**
     * Loads demonstration data into the database for testing and demonstration purposes.
     * This includes sample suppliers, products, and customers.
     */
    public void loadDemoData() {
        // Add sample suppliers
        addSupplier("Tech Supplies Inc", "sales@techsupplies.com", "555-0100");
        addSupplier("Office Depot", "orders@officedepot.com", "555-0200");
        addSupplier("Electronics Wholesale", "contact@elecwholesale.com", "555-0300");

        // Add sample products
        addItem("P001", "External Hard Drive 1TB", "Electronics", 89.99, 50, null, 10);
        addItem("P002", "Stainless Steel Spoons (12-pack)", "Kitchenware", 14.99, 200, null, 30);
        addItem("P003", "USB Flash Drive 32GB", "Electronics", 12.50, 20, null, 15);
        addItem("P004", "Office Notebook A4", "Stationery", 3.99, 150, null, 40);
        addItem("P005", "Wireless Mouse", "Electronics", 24.99, 75, null, 20);

        // Add sample customers
        addCustomer("John Smith", "john.smith@email.com", "555-1001");
        addCustomer("Jane Doe", "jane.doe@email.com", "555-1002");
        addCustomer("Bob Johnson", "bob.johnson@email.com", "555-1003");

        System.out.println("Demo data loaded successfully into database.");
    }

    /**
     * Closes the database connection when the service is shutting down.
     * Should be called before application exit.
     */
    public void shutdown() {
        DatabaseManager.getInstance().closeConnection();
    }
}
