package com.inventory;

import com.inventory.service.InventoryService;
import com.inventory.ui.InventoryAppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventoryService service = new InventoryService();
            InventoryAppFrame frame = new InventoryAppFrame(service);
            frame.setVisible(true);
        });
    }
}
