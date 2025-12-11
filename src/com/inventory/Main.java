package com.inventory;

import com.inventory.db.DatabaseHelper;
import com.inventory.service.AuthService;
import com.inventory.service.InventoryService;
import com.inventory.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            InventoryService inventoryService = new InventoryService();
            DatabaseHelper.initializeAndLoadSampleData(inventoryService);

            AuthService authService = new AuthService();

            LoginFrame login = new LoginFrame(authService, inventoryService);
            login.setVisible(true);
        });
    }
}
