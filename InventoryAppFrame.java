package com.inventory.ui;

import com.inventory.model.Product;
import com.inventory.model.Report;
import com.inventory.model.Supplier;
import com.inventory.service.InventoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class InventoryAppFrame extends JFrame {

    private InventoryService service;

    // Items tab components
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JTextField txtProdId;
    private JTextField txtProdName;
    private JTextField txtProdCategory;
    private JTextField txtProdPrice;
    private JTextField txtProdStock;
    private JTextField txtProdReorder;

    // Supplier tab components
    private DefaultTableModel supplierTableModel;
    private JTable supplierTable;
    private JTextField txtSupName;
    private JTextField txtSupEmail;
    private JTextField txtSupPhone;

    // Reports tab components
    private JTextArea txtReportArea;

    public InventoryAppFrame(InventoryService service) {
        this.service = service;
        this.service.loadDemoData();

        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initUI();
        refreshProductTable();
        refreshSupplierTable();
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Items", createItemsPanel()); //Items
        tabbedPane.addTab("Suppliers", createSuppliersPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // Items Tab

    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // top form
        JPanel form = new JPanel(new GridLayout(2, 6, 5, 5));
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

        // Center table
        productTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Category", "Price", "Stock", "Reorder"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttons = new JPanel();
        JButton btnAdd = new JButton("Add/Update Item");
        JButton btnIncreaseStock = new JButton("Increase Stock");
        JButton btnDecreaseStock = new JButton("Decrease Stock");
        JButton btnRefresh = new JButton("Refresh");

        buttons.add(btnAdd);
        buttons.add(btnIncreaseStock);
        buttons.add(btnDecreaseStock);
        buttons.add(btnRefresh);

        panel.add(buttons, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> onAddOrUpdateItem());
        btnIncreaseStock.addActionListener(e -> onChangeStock(true));
        btnDecreaseStock.addActionListener(e -> onChangeStock(false));
        btnRefresh.addActionListener(e -> refreshProductTable());

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

            JOptionPane.showMessageDialog(this, "ID and Name ");
            if (id.isEmpty() || name.isEmpty()) {
                return;
            }

            // expiryDate = null for now i guess
            service.addItem(id, name, category, price, stock, null, reorder);
            refreshProductTable();
            JOptionPane.showMessageDialog(this, "Item added/updated successfully ");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fixed: " + ex.getMessage());
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

    // Suppliers Tab

    private JPanel createSuppliersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 3, 5, 5));
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

        // Table
        supplierTableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Phone"}, 0);
        supplierTable = new JTable(supplierTableModel);
        JScrollPane scroll = new JScrollPane(supplierTable);
        panel.add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();
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

    // ---------- Reports Tab ----------

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        txtReportArea = new JTextArea();
        txtReportArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtReportArea);

        JPanel buttons = new JPanel();
        JButton btnStockReport = new JButton("Generate Stock Report");
        JButton btnLowStockReport = new JButton("Generate Low Stock Report");

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
}
