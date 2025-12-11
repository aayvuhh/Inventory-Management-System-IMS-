package com.inventory.service;

import com.inventory.db.DatabaseHelper;
import com.inventory.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {

    Map<String, Product> products = new HashMap<>();
    Map<Integer, Supplier> suppliers = new HashMap<>();
    Map<Integer, Customer> customers = new HashMap<>();
    List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    List<Report> reports = new ArrayList<>();
    List<Sale> sales = new ArrayList<>();
    List<StockRequest> stockRequests = new ArrayList<>();

    int nextSupplierId = 1;
    int nextCustomerId = 1;
    int nextOrderId = 1;
    int nextReportId = 1;
    int nextStockRequestId = 1;

    private double totalRevenue = 0.0;
    private double totalProfit = 0.0;

    private InventoryManager defaultManager =
            new InventoryManager(1, "Default Manager", "manager@example.com");

    // ---------- Products ----------

    public Product addItem(String id, String name, String category, double unitPrice,
                           int stockLevel, LocalDate expiryDate, int reorderLevel) {
        Product product = new Product(id, name, category, unitPrice, stockLevel, expiryDate, reorderLevel);
        products.put(id, product);
        return product;
    }

    public Product addItemFromDatabase(String id, String name, String category, double unitPrice,
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
        return products.values().stream()
                .filter(p -> p.getId().toLowerCase().contains(lower)
                        || p.getName().toLowerCase().contains(lower)
                        || p.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Product> getLowStockProducts() {
        return products.values().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }

    // ---------- Suppliers ----------

    public Supplier addSupplier(String name, String email, String phone) {
        Supplier supplier = new Supplier(nextSupplierId++, name, email, phone);
        suppliers.put(supplier.getId(), supplier);
        return supplier;
    }

    public Supplier addSupplierFromDatabase(int id, String name, String email, String phone) {
        Supplier supplier = new Supplier(id, name, email, phone);
        suppliers.put(id, supplier);
        nextSupplierId = Math.max(nextSupplierId, id + 1);
        return supplier;
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    public Supplier getSupplierById(int id) {
        return suppliers.get(id);
    }

    // ---------- Customers ----------

    public Customer addCustomer(String name, String email, String phone) {
        Customer customer = new Customer(nextCustomerId++, name, email, phone);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer addCustomerFromDatabase(int id, String name, String email, String phone) {
        Customer customer = new Customer(id, name, email, phone);
        customers.put(id, customer);
        nextCustomerId = Math.max(nextCustomerId, id + 1);
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

    public PurchaseOrder addPurchaseOrderFromDatabase(int id, int supplierId,
                                                      LocalDate createdDate, String statusStr) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) {
            return null;
        }
        PurchaseOrder po = new PurchaseOrder(id, supplier, defaultManager);
        po.setCreatedDate(createdDate);
        try {
            po.setStatus(OrderStatus.valueOf(statusStr.toUpperCase()));
        } catch (Exception e) {
            po.setStatus(OrderStatus.CREATED);
        }
        purchaseOrders.add(po);
        nextOrderId = Math.max(nextOrderId, id + 1);
        return po;
    }

    public void addOrderItemFromDatabase(int poId, String productId,
                                         int quantity, double unitPrice) {
        PurchaseOrder po = findPurchaseOrderById(poId);
        Product product = products.get(productId);
        if (po == null || product == null) {
            return;
        }
        OrderItem item = new OrderItem(product, quantity, unitPrice);
        po.addItem(item);
    }

    private PurchaseOrder findPurchaseOrderById(int id) {
        for (PurchaseOrder po : purchaseOrders) {
            if (po.getId() == id) return po;
        }
        return null;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return new ArrayList<>(purchaseOrders);
    }

    // ---------- Sales / Revenue / Profit ----------

    public void recordSale(String productId, int quantity, double salePrice, Account seller) {
        Product product = products.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        double costPrice = product.getUnitPrice();
        updateStock(productId, -quantity);

        Sale sale = new Sale(0, product, quantity, salePrice, costPrice, LocalDate.now(), seller);
        sales.add(sale);
        totalRevenue += sale.getRevenue();
        totalProfit += sale.getProfit();

        try {
            DatabaseHelper.insertSale(productId, quantity, salePrice, costPrice, sale.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSaleFromDatabase(int id, String productId, int quantity,
                                    double salePrice, double costPrice, LocalDate date) {
        Product product = products.get(productId);
        if (product == null) return;
        Sale sale = new Sale(id, product, quantity, salePrice, costPrice, date, null);
        sales.add(sale);
        totalRevenue += sale.getRevenue();
        totalProfit += sale.getProfit();
    }

    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalProfit() { return totalProfit; }

    // ---------- Stock Requests ----------

    public StockRequest createStockRequest(Account requester,
                                           String productId,
                                           int quantity,
                                           double costPrice,
                                           double salePrice) {
        Product product = products.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        double expectedRevenue = salePrice * quantity;
        double expectedProfit = (salePrice - costPrice) * quantity;

        StockRequest req = new StockRequest(
                nextStockRequestId++,
                product,
                quantity,
                costPrice,
                salePrice,
                expectedRevenue,
                expectedProfit,
                requester,
                "PENDING",
                LocalDateTime.now()
        );

        stockRequests.add(req);
        return req;
    }

    public List<StockRequest> getStockRequestsForUser(Account user) {
        List<StockRequest> list = new ArrayList<>();
        for (StockRequest r : stockRequests) {
            if (r.getRequestedBy() != null &&
                    r.getRequestedBy().getId() == user.getId()) {
                list.add(r);
            }
        }
        return list;
    }

    public List<StockRequest> getAllStockRequests() {
        return new ArrayList<>(stockRequests);
    }

    public void approveStockRequest(int requestId, Account manager) {
        StockRequest req = findStockRequestById(requestId);
        if (req == null) {
            throw new IllegalArgumentException("Stock request not found: " + requestId);
        }
        if (!"PENDING".equals(req.getStatus())) {
            throw new IllegalStateException("Only pending requests can be approved.");
        }

        req.approve(manager);
        updateStock(req.getProduct().getId(), req.getQuantity());
    }

    public void rejectStockRequest(int requestId, Account manager) {
        StockRequest req = findStockRequestById(requestId);
        if (req == null) {
            throw new IllegalArgumentException("Stock request not found: " + requestId);
        }
        if (!"PENDING".equals(req.getStatus())) {
            throw new IllegalStateException("Only pending requests can be rejected.");
        }

        req.reject(manager);
    }

    private StockRequest findStockRequestById(int id) {
        for (StockRequest r : stockRequests) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    // ---------- Employee performance stats ----------

    public List<EmployeeStats> getEmployeeStats() {
        Map<Integer, EmployeeStats> map = new HashMap<>();

        for (Sale sale : sales) {
            Account seller = sale.getSoldBy();
            if (seller == null) continue;
            if (seller.getRole() != UserRole.EMPLOYEE) continue;

            EmployeeStats stats = map.get(seller.getId());
            if (stats == null) {
                stats = new EmployeeStats(seller);
                map.put(seller.getId(), stats);
            }
            stats.addSale(sale.getRevenue(), sale.getProfit());
        }

        return new ArrayList<>(map.values());
    }

    // ---------- Reports ----------

    public Report generateStockReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("STOCK SUMMARY REPORT\n");
        sb.append("====================\n");
        for (Product p : products.values()) {
            sb.append(String.format("%s (%s): %d units @ %.2f\n",
                    p.getName(), p.getId(), p.getStockLevel(), p.getUnitPrice()));
        }
        Report report = new Report(nextReportId++, defaultManager,
                ReportType.STOCK_SUMMARY, sb.toString());
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
        Report report = new Report(nextReportId++, defaultManager,
                ReportType.LOW_STOCK, sb.toString());
        reports.add(report);
        return report;
    }

    public List<Report> getAllReports() {
        return new ArrayList<>(reports);
    }
}
