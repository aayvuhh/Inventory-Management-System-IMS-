package com.inventory.service;

import com.inventory.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//Mr.Robot lied to me
public class InventoryService {

    private Map<String, Product> products = new HashMap<>();
    private Map<Integer, Supplier> suppliers = new HashMap<>();
    private Map<Integer, Customer> customers = new HashMap<>();
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private List<Report> reports = new ArrayList<>();

    private int nextSupplierId = 1;
    private int nextCustomerId = 1;
    private int nextOrderId = 1;
    private int nextReportId = 1;

    private InventoryManager defaultManager =
            new InventoryManager(1, "Dwayne Johnson", "therock@therock.com");

    // Items and Products

    public Product addItem(String id, String name, String category, double unitPrice,
                           int stockLevel, LocalDate expiryDate, int reorderLevel) {

        Product product = new Product(id, name, category, unitPrice, stockLevel, expiryDate, reorderLevel);
        products.put(id, product);
        return product;
    }

    public void updateStock(String productId, int deltaQuantity) {
        Product product = products.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        int newStock = product.getStockLevel() + deltaQuantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }
        product.setStockLevel(newStock);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> searchItems(String keyword) {
        String lower = keyword.toLowerCase();
        return products.values().stream().filter(p -> p.getId().toLowerCase().contains(lower)
                        || p.getName().toLowerCase().contains(lower)
                        || p.getCategory().toLowerCase().contains(lower)).collect(Collectors.toList());
    }

    public List<Product> getLowStockProducts() {
        return products.values().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }

    // Drug Suppliers

    public Supplier addSupplier(String name, String email, String phone) {
        Supplier supplier = new Supplier(nextSupplierId++, name, email, phone);
        suppliers.put(supplier.getId(), supplier);
        return supplier;
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public Supplier getSupplierById(int id) {
        return suppliers.get(id);
    }

    // Drug Customers

    public Customer addCustomer(String name, String email, String phone) {
        Customer customer = new Customer(nextCustomerId++, name, email, phone);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    // ---------- Purchase Orders ----------

    public PurchaseOrder createPurchaseOrder(int supplierId) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found: " + supplierId);
        }
        PurchaseOrder po = new PurchaseOrder(nextOrderId++, supplier, defaultManager);
        purchaseOrders.add(po);
        return po;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return new ArrayList<>(purchaseOrders);
    }

    // Sales and returnth products

    public void issueProducts(String productId, int quantity, int customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer dont exist bruh: " + customerId);
        }
        updateStock(productId, -quantity);
        // record sales transaction linked to customer in full system
    }

    public void returnProducts(String productId, int quantity, int customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer will not be found: " + customerId);
        }
        updateStock(productId, quantity);
        // record return transaction in full system
    }

    // School Reports

    public Report generateStockReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("STOCK SUMMARY REPORT\n");
        sb.append("====================\n");
        for (Product p : products.values()) {
            sb.append(String.format("%s (%s): %d units @ %.2f\n",
                    p.getName(), p.getId(), p.getStockLevel(), p.getUnitPrice()));
        }
        Report report = new Report(nextReportId++, defaultManager, ReportType.Stalk_Summary, sb.toString());
        reports.add(report);
        return report;
    }

    public Report generateLowStockReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("LOW STOCK REPORT\n");
        sb.append("================\n");
        for (Product p : getLowStockProducts()) {
            sb.append(String.format("%s (%s): %d units (reorder level: %d)\n",
                    p.getName(), p.getId(), p.getStockLevel(), p.getReorderLevel()));
        }
        Report report = new Report(nextReportId++, defaultManager, ReportType.Low_Stock, sb.toString());
        reports.add(report);
        return report;
    }

    public List<Report> getAllReports() {
        return new ArrayList<>(reports);
    }

    // Demo Data

    public void loadDemoData() {
        addSupplier("Pablo Escobar", "weed@yahoo.com", "911-911-9111");
        addSupplier("Hitler", "theRealHitler@outlook.com", "999-111-2222");

        addItem("P001", "Jew", "Human", 21.22, 50, null, 10);
        addItem("P002", "KSI", "Human", 0.99, 200, null, 30);
        addItem("P003", "USB", "Electronics", 12.50, 20, null, 5);

        addCustomer("Batman", "alfreddabuTTler@gmail.com", "555-1234");
        addCustomer("Superman", "whatAreGlasses@proton.me", "555-5678");
    }
}
