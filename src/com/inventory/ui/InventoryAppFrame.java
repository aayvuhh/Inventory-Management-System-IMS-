package com.inventory.ui;

import com.inventory.model.*;
import com.inventory.service.AuthService;
import com.inventory.service.InventoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class InventoryAppFrame extends JFrame {

    private final InventoryService service;
    private final AuthService authService;
    private final Account currentUser;

    // Items tab
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JTextField txtProdId;
    private JTextField txtProdName;
    private JTextField txtProdCategory;
    private JTextField txtProdPrice;
    private JTextField txtProdStock;
    private JTextField txtProdReorder;

    // Supplier tab
    private DefaultTableModel supplierTableModel;
    private JTable supplierTable;
    private JTextField txtSupName;
    private JTextField txtSupEmail;
    private JTextField txtSupPhone;

    // Reports tab
    private JTextArea txtReportArea;

    // Purchase Orders tab
    private DefaultTableModel poTableModel;
    private JTable poTable;
    private DefaultTableModel poItemsTableModel;
    private JTable poItemsTable;
    private JLabel lblPoSummary;

    // Admin tab
    private DefaultTableModel resetTableModel;
    private JTable resetTable;
    private DefaultTableModel employeeStatsTableModel;
    private JTable employeeStatsTable;

    // Stock Requests tab
    private DefaultTableModel stockReqTableModel;
    private JTable stockReqTable;
    private JTextField txtReqProductId;
    private JTextField txtReqQty;
    private JTextField txtReqCost;
    private JTextField txtReqSale;

    // Revenue / Profit labels (manager only)
    private JLabel lblRevenue;
    private JLabel lblProfit;

    public InventoryAppFrame(InventoryService service, AuthService authService, Account currentUser) {
        this.service = service;
        this.authService = authService;
        this.currentUser = currentUser;

        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        initUI();
        refreshProductTable();
        refreshSupplierTable();
        if (currentUser.getRole() == UserRole.MANAGER) {
            refreshPurchaseOrdersTable();
        }
        refreshStockRequestsTable();
        updateRevenueProfitLabels();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        JPanel header = new JPanel(new BorderLayout());

        JLabel lblUser = new JLabel("Logged in as: " + currentUser.getFullName()
                + " (" + currentUser.getRole() + ")");
        header.add(lblUser, BorderLayout.WEST);

        JPanel rightHeader = new JPanel(new BorderLayout());

        lblRevenue = new JLabel();
        lblProfit = new JLabel();
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.add(lblRevenue);
        statsPanel.add(lblProfit);

        JButton btnLogout = new JButton("Log Out");
        btnLogout.addActionListener(e -> onLogout());

        rightHeader.add(statsPanel, BorderLayout.CENTER);
        rightHeader.add(btnLogout, BorderLayout.SOUTH);

        header.add(rightHeader, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Items", createItemsPanel());
        tabbedPane.addTab("Suppliers", createSuppliersPanel());

        if (currentUser.getRole() == UserRole.MANAGER) {
            tabbedPane.addTab("Purchase Orders", createPurchaseOrdersPanel());
        }

        tabbedPane.addTab("Stock Requests", createStockRequestsPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        if (currentUser.getRole() == UserRole.MANAGER) {
            tabbedPane.addTab("Admin", createAdminPanel());
        }

        root.add(tabbedPane, BorderLayout.CENTER);
    }

    private void onLogout() {
        this.dispose();
        LoginFrame login = new LoginFrame(authService, service);
        login.setVisible(true);
    }

    // ---------- Items Tab ----------

    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(2, 6, 5, 5));
        form.setBorder(new TitledBorder("Product Details"));

        txtProdId = new JTextField();
        txtProdName = new JTextField();
        txtProdCategory = new JTextField();
        txtProdPrice = new JTextField();
        txtProdStock = new JTextField();
        txtProdReorder = new JTextField();

        form.add(new JLabel("ID:"));
        form.add(new JLabel("Name:"));
        form.add(new JLabel("Category:"));
        form.add(new JLabel("Price:"));
        form.add(new JLabel("Stock:"));
        form.add(new JLabel("Reorder Level:"));

        form.add(txtProdId);
        form.add(txtProdName);
        form.add(txtProdCategory);
        form.add(txtProdPrice);
        form.add(txtProdStock);
        form.add(txtProdReorder);

        panel.add(form, BorderLayout.NORTH);

        productTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Category", "Price", "Stock", "Reorder"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(productTableModel);
        styleTable(productTable);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(new TitledBorder("Inventory"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add / Update Item");
        JButton btnIncreaseStock = new JButton("Increase Stock");
        JButton btnDecreaseStock = new JButton("Decrease Stock");
        JButton btnRecordSale = new JButton("Record Sale");
        JButton btnRefresh = new JButton("Refresh");

        buttons.add(btnAdd);
        buttons.add(btnIncreaseStock);
        buttons.add(btnDecreaseStock);
        buttons.add(btnRecordSale);
        buttons.add(btnRefresh);

        panel.add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> onAddOrUpdateItem());
        btnIncreaseStock.addActionListener(e -> onChangeStock(true));
        btnDecreaseStock.addActionListener(e -> onChangeStock(false));
        btnRefresh.addActionListener(e -> refreshProductTable());
        btnRecordSale.addActionListener(e -> onRecordSale());

        return panel;
    }

    private void onAddOrUpdateItem() {
        try {
            String id = txtProdId.getText().trim();
            String name = txtProdName.getText().trim();
            String category = txtProdCategory.getText().trim();
            double price = Double.parseDouble(txtProdPrice.getText().trim());
            int stock = Integer.parseInt(txtProdStock.getText().trim());
            int reorder = Integer.parseInt(txtProdReorder.getText().trim());

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name are required");
                return;
            }

            service.addItem(id, name, category, price, stock, null, reorder);
            refreshProductTable();
            JOptionPane.showMessageDialog(this, "Item added/updated successfully");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onChangeStock(boolean increase) {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }
        String productId = (String) productTableModel.getValueAt(row, 0);
        String qty = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (qty == null) return;
        try {
            int q = Integer.parseInt(qty.trim());
            if (!increase) q = -q;
            service.updateStock(productId, q);
            refreshProductTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onRecordSale() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }
        String productId = (String) productTableModel.getValueAt(row, 0);

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity sold:");
        if (qtyStr == null) return;
        String priceStr = JOptionPane.showInputDialog(this, "Enter sale price per unit:");
        if (priceStr == null) return;

        try {
            int qty = Integer.parseInt(qtyStr.trim());
            double salePrice = Double.parseDouble(priceStr.trim());
            service.recordSale(productId, qty, salePrice, currentUser);
            refreshProductTable();
            updateRevenueProfitLabels();
            JOptionPane.showMessageDialog(this, "Sale recorded successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refreshProductTable() {
        List<Product> products = service.getAllProducts();
        productTableModel.setRowCount(0);
        for (Product p : products) {
            productTableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getUnitPrice(),
                    p.getStockLevel(),
                    p.getReorderLevel()
            });
        }
    }

    // ---------- Suppliers Tab ----------

    private JPanel createSuppliersPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(2, 3, 5, 5));
        form.setBorder(new TitledBorder("Supplier Details"));

        txtSupName = new JTextField();
        txtSupEmail = new JTextField();
        txtSupPhone = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(new JLabel("Email:"));
        form.add(new JLabel("Phone:"));

        form.add(txtSupName);
        form.add(txtSupEmail);
        form.add(txtSupPhone);

        panel.add(form, BorderLayout.NORTH);

        supplierTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Phone"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        supplierTable = new JTable(supplierTableModel);
        styleTable(supplierTable);
        JScrollPane scroll = new JScrollPane(supplierTable);
        scroll.setBorder(new TitledBorder("Suppliers"));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Supplier");
        JButton btnRefresh = new JButton("Refresh");
        buttons.add(btnAdd);
        buttons.add(btnRefresh);
        panel.add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> onAddSupplier());
        btnRefresh.addActionListener(e -> refreshSupplierTable());

        return panel;
    }

    private void onAddSupplier() {
        String name = txtSupName.getText().trim();
        String email = txtSupEmail.getText().trim();
        String phone = txtSupPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
            return;
        }

        service.addSupplier(name, email, phone);
        refreshSupplierTable();
        JOptionPane.showMessageDialog(this, "Supplier added");
    }

    private void refreshSupplierTable() {
        List<Supplier> suppliers = service.getAllSuppliers();
        supplierTableModel.setRowCount(0);
        for (Supplier s : suppliers) {
            supplierTableModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getContactEmail(),
                    s.getPhone()
            });
        }
    }

    // ---------- Purchase Orders Tab ----------

    private JPanel createPurchaseOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        poTableModel = new DefaultTableModel(
                new Object[]{"ID", "Supplier", "Date", "Status", "Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        poTable = new JTable(poTableModel);
        styleTable(poTable);
        JScrollPane poScroll = new JScrollPane(poTable);
        poScroll.setBorder(new TitledBorder("Purchase Orders"));

        poItemsTableModel = new DefaultTableModel(
                new Object[]{"Product", "Qty", "Unit Price", "Line Total"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        poItemsTable = new JTable(poItemsTableModel);
        styleTable(poItemsTable);
        JScrollPane itemsScroll = new JScrollPane(poItemsTable);
        itemsScroll.setBorder(new TitledBorder("Order Items"));

        lblPoSummary = new JLabel("Select a purchase order to view details.");
        lblPoSummary.setBorder(new EmptyBorder(4, 4, 4, 4));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(itemsScroll, BorderLayout.CENTER);
        bottomPanel.add(lblPoSummary, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, poScroll, bottomPanel);
        split.setDividerLocation(220);
        split.setResizeWeight(0.5);

        panel.add(split, BorderLayout.CENTER);

        ListSelectionListener listener = e -> {
            if (!e.getValueIsAdjusting()) {
                onPurchaseOrderSelected();
            }
        };
        poTable.getSelectionModel().addListSelectionListener(listener);

        return panel;
    }

    private void refreshPurchaseOrdersTable() {
        if (poTableModel == null) return;
        List<PurchaseOrder> pos = service.getAllPurchaseOrders();
        poTableModel.setRowCount(0);
        for (PurchaseOrder po : pos) {
            poTableModel.addRow(new Object[]{
                    po.getId(),
                    po.getSupplier() != null ? po.getSupplier().getName() : "",
                    po.getCreatedDate(),
                    po.getStatus(),
                    String.format("%.2f", po.getTotalAmount())
            });
        }
        if (poItemsTableModel != null) {
            poItemsTableModel.setRowCount(0);
        }
        if (lblPoSummary != null) {
            lblPoSummary.setText("Select a purchase order to view details.");
        }
    }

    private void onPurchaseOrderSelected() {
        int row = poTable.getSelectedRow();
        if (row < 0) {
            poItemsTableModel.setRowCount(0);
            lblPoSummary.setText("Select a purchase order to view details.");
            return;
        }

        int poId = (int) poTableModel.getValueAt(row, 0);
        PurchaseOrder selected = null;
        for (PurchaseOrder po : service.getAllPurchaseOrders()) {
            if (po.getId() == poId) {
                selected = po;
                break;
            }
        }
        if (selected == null) return;

        poItemsTableModel.setRowCount(0);
        for (OrderItem item : selected.getItems()) {
            poItemsTableModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    String.format("%.2f", item.getLineTotal())
            });
        }

        lblPoSummary.setText(String.format(
                "PO #%d | Supplier: %s | Date: %s | Status: %s | Total: %.2f",
                selected.getId(),
                selected.getSupplier() != null ? selected.getSupplier().getName() : "",
                selected.getCreatedDate(),
                selected.getStatus(),
                selected.getTotalAmount()
        ));
    }

    // ---------- Stock Requests Tab ----------

    private JPanel createStockRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(2, 4, 5, 5));
        form.setBorder(new TitledBorder("Request Stock (Employee)"));

        txtReqProductId = new JTextField();
        txtReqQty = new JTextField();
        txtReqCost = new JTextField();
        txtReqSale = new JTextField();

        form.add(new JLabel("Product ID:"));
        form.add(new JLabel("Quantity:"));
        form.add(new JLabel("Cost per Unit:"));
        form.add(new JLabel("Sale Price per Unit:"));

        form.add(txtReqProductId);
        form.add(txtReqQty);
        form.add(txtReqCost);
        form.add(txtReqSale);

        if (currentUser.getRole() == UserRole.EMPLOYEE) {
            panel.add(form, BorderLayout.NORTH);
        }

        stockReqTableModel = new DefaultTableModel(
                new Object[]{"ID", "Employee", "Product", "Qty",
                        "Cost", "Sale", "Exp Revenue", "Exp Profit", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        stockReqTable = new JTable(stockReqTableModel);
        styleTable(stockReqTable);
        JScrollPane scroll = new JScrollPane(stockReqTable);
        scroll.setBorder(new TitledBorder("Stock Requests"));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (currentUser.getRole() == UserRole.EMPLOYEE) {
            JButton btnSubmit = new JButton("Submit Request");
            btnSubmit.addActionListener(e -> onSubmitStockRequest());
            buttons.add(btnSubmit);
        }

        if (currentUser.getRole() == UserRole.MANAGER) {
            JButton btnApprove = new JButton("Approve");
            JButton btnReject = new JButton("Reject");
            JButton btnRefresh = new JButton("Refresh");
            btnApprove.addActionListener(e -> onApproveStockRequest());
            btnReject.addActionListener(e -> onRejectStockRequest());
            btnRefresh.addActionListener(e -> refreshStockRequestsTable());
            buttons.add(btnRefresh);
            buttons.add(btnApprove);
            buttons.add(btnReject);
        }

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void onSubmitStockRequest() {
        try {
            String productId = txtReqProductId.getText().trim();
            int qty = Integer.parseInt(txtReqQty.getText().trim());
            double cost = Double.parseDouble(txtReqCost.getText().trim());
            double sale = Double.parseDouble(txtReqSale.getText().trim());

            service.createStockRequest(currentUser, productId, qty, cost, sale);
            refreshStockRequestsTable();
            JOptionPane.showMessageDialog(this, "Stock request submitted.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onApproveStockRequest() {
        int row = stockReqTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        int id = (int) stockReqTableModel.getValueAt(row, 0);
        try {
            service.approveStockRequest(id, currentUser);
            refreshStockRequestsTable();
            refreshProductTable();
            JOptionPane.showMessageDialog(this, "Request approved and stock updated.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onRejectStockRequest() {
        int row = stockReqTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        int id = (int) stockReqTableModel.getValueAt(row, 0);
        try {
            service.rejectStockRequest(id, currentUser);
            refreshStockRequestsTable();
            JOptionPane.showMessageDialog(this, "Request rejected.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refreshStockRequestsTable() {
        if (stockReqTableModel == null) return;

        List<StockRequest> list;
        if (currentUser.getRole() == UserRole.EMPLOYEE) {
            list = service.getStockRequestsForUser(currentUser);
        } else {
            list = service.getAllStockRequests();
        }

        stockReqTableModel.setRowCount(0);
        for (StockRequest r : list) {
            stockReqTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getRequestedBy() != null ? r.getRequestedBy().getFullName() : "",
                    r.getProduct().getName(),
                    r.getQuantity(),
                    r.getCostPrice(),
                    r.getSalePrice(),
                    r.getExpectedRevenue(),
                    r.getExpectedProfit(),
                    r.getStatus()
            });
        }
    }

    // ---------- Reports Tab ----------

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        txtReportArea = new JTextArea();
        txtReportArea.setEditable(false);
        txtReportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtReportArea);
        scroll.setBorder(new TitledBorder("Report Output"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnStockReport = new JButton("Stock Report");
        JButton btnLowStockReport = new JButton("Low Stock Report");

        buttons.add(btnStockReport);
        buttons.add(btnLowStockReport);

        btnStockReport.addActionListener(e -> onGenerateStockReport());
        btnLowStockReport.addActionListener(e -> onGenerateLowStockReport());

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void onGenerateStockReport() {
        Report r = service.generateStockReport();
        txtReportArea.setText(r.getContent());
    }

    private void onGenerateLowStockReport() {
        Report r = service.generateLowStockReport();
        txtReportArea.setText(r.getContent());
    }

    // ---------- Admin Tab ----------

    private JPanel createAdminPanel() {
        JTabbedPane adminTabs = new JTabbedPane();

        JPanel resetPanel = new JPanel(new BorderLayout(8, 8));
        resetTableModel = new DefaultTableModel(
                new Object[]{"ID", "Email", "Requested At"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        resetTable = new JTable(resetTableModel);
        styleTable(resetTable);
        JScrollPane resetScroll = new JScrollPane(resetTable);
        resetScroll.setBorder(new TitledBorder("Password Reset Requests"));
        resetPanel.add(resetScroll, BorderLayout.CENTER);

        JPanel resetButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        resetButtons.add(btnRefresh);
        resetButtons.add(btnApprove);
        resetButtons.add(btnReject);
        resetPanel.add(resetButtons, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadResetRequests());
        btnApprove.addActionListener(e -> approveSelectedReset());
        btnReject.addActionListener(e -> rejectSelectedReset());

        adminTabs.addTab("Password Resets", resetPanel);

        JPanel perfPanel = new JPanel(new BorderLayout(8, 8));
        employeeStatsTableModel = new DefaultTableModel(
                new Object[]{"⭐", "Name", "Email", "Total Revenue", "Total Profit",
                        "Commission (10%)", "Salary"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        employeeStatsTable = new JTable(employeeStatsTableModel);
        styleTable(employeeStatsTable);
        JScrollPane perfScroll = new JScrollPane(employeeStatsTable);
        perfScroll.setBorder(new TitledBorder("Employee Sales & Salary"));
        perfPanel.add(perfScroll, BorderLayout.CENTER);

        JButton btnLoadStats = new JButton("Refresh Stats");
        btnLoadStats.addActionListener(e -> loadEmployeeStats());
        JPanel perfButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        perfButtons.add(btnLoadStats);
        perfPanel.add(perfButtons, BorderLayout.SOUTH);

        adminTabs.addTab("Employee Performance", perfPanel);

        loadResetRequests();
        loadEmployeeStats();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(adminTabs, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadResetRequests() {
        if (resetTableModel == null) return;
        try {
            resetTableModel.setRowCount(0);
            List<PasswordResetRequest> list = authService.getPendingResetRequests();
            for (PasswordResetRequest req : list) {
                resetTableModel.addRow(new Object[]{
                        req.getId(),
                        req.getEmail(),
                        req.getRequestedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading reset requests: " + e.getMessage());
        }
    }

    private void approveSelectedReset() {
        int row = resetTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        int reqId = (int) resetTableModel.getValueAt(row, 0);
        try {
            String newPass = authService.approveReset(reqId);
            JOptionPane.showMessageDialog(this,
                    "Request approved.\nNew password: " + newPass +
                            "\nPlease share it securely with the employee.");
            loadResetRequests();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void rejectSelectedReset() {
        int row = resetTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request first.");
            return;
        }
        int reqId = (int) resetTableModel.getValueAt(row, 0);
        try {
            authService.rejectReset(reqId);
            JOptionPane.showMessageDialog(this, "Request rejected.");
            loadResetRequests();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadEmployeeStats() {
        if (employeeStatsTableModel == null) return;
        employeeStatsTableModel.setRowCount(0);

        List<EmployeeStats> stats = service.getEmployeeStats();
        if (stats.isEmpty()) return;

        EmployeeStats best = stats.stream()
                .max(Comparator.comparingDouble(EmployeeStats::getCommission))
                .orElse(null);

        for (EmployeeStats es : stats) {
            boolean isBest = (best != null &&
                    Double.compare(es.getCommission(), best.getCommission()) == 0);

            String star = isBest ? "⭐" : "";

            employeeStatsTableModel.addRow(new Object[]{
                    star,
                    es.getEmployee().getFullName(),
                    es.getEmployee().getEmail(),
                    String.format("%.2f", es.getTotalRevenue()),
                    String.format("%.2f", es.getTotalProfit()),
                    String.format("%.2f", es.getCommission()),
                    String.format("%.2f", es.getSalary())
            });
        }
    }

    // ---------- Helpers ----------

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
    }

    private void updateRevenueProfitLabels() {
        if (currentUser.getRole() == UserRole.MANAGER) {
            lblRevenue.setText(String.format("Total Revenue: $%.2f", service.getTotalRevenue()));
            lblProfit.setText(String.format("Total Profit: $%.2f", service.getTotalProfit()));
        } else {
            lblRevenue.setText("");
            lblProfit.setText("");
        }
    }
}
