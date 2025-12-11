package com.inventory.ui;

import com.inventory.model.Account;
import com.inventory.service.AuthService;
import com.inventory.service.InventoryService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthService authService;
    private final InventoryService inventoryService;

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginFrame(AuthService authService, InventoryService inventoryService) {
        this.authService = authService;
        this.inventoryService = inventoryService;

        setTitle("IMS Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // smaller, more compact size
        setSize(340, 210);
        setResizable(false);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setContentPane(root);

        // Center panel with a subtle title = sleek
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(new TitledBorder("Sign In"));

        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        fields.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(18); // smaller width
        txtEmail.setMargin(new Insets(2, 4, 2, 4));
        fields.add(txtEmail, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        fields.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(18); // smaller width
        txtPassword.setMargin(new Insets(2, 4, 2, 4));
        fields.add(txtPassword, gbc);

        centerPanel.add(fields, BorderLayout.CENTER);

        root.add(centerPanel, BorderLayout.CENTER);

        // Buttons row (slim buttons)
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        JButton btnForgot = new JButton("Forgot?");
        JButton btnExit = new JButton("Exit");

        // Slim margins = sleek
        Insets btnInset = new Insets(2, 10, 2, 10);
        btnLogin.setMargin(btnInset);
        btnRegister.setMargin(btnInset);
        btnForgot.setMargin(new Insets(2, 6, 2, 6));
        btnExit.setMargin(btnInset);

        buttons.add(btnForgot);
        buttons.add(btnRegister);
        buttons.add(btnLogin);
        buttons.add(btnExit);

        root.add(buttons, BorderLayout.SOUTH);

        // Actions
        btnLogin.addActionListener(e -> onLogin());
        btnRegister.addActionListener(e -> onRegister());
        btnForgot.addActionListener(e -> onForgotPassword());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            Account account = authService.login(email, password);
            InventoryAppFrame app = new InventoryAppFrame(inventoryService, authService, account);
            app.setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegister() {
        RegistrationDialog dialog = new RegistrationDialog(this, authService);
        dialog.setVisible(true);
    }

    private void onForgotPassword() {
        String email = JOptionPane.showInputDialog(this,
                "Enter your registered email:", "Forgot Password", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;
        try {
            authService.requestPasswordReset(email.trim());
            JOptionPane.showMessageDialog(this,
                    "Password reset request submitted. The manager will review it.",
                    "Request Sent", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
