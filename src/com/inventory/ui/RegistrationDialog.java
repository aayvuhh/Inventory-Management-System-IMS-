package com.inventory.ui;

import com.inventory.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegistrationDialog extends JDialog {

    private final AuthService authService;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;

    public RegistrationDialog(Frame owner, AuthService authService) {
        super(owner, "Register New Employee", true);
        this.authService = authService;

        setSize(400, 320);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fields = new JPanel(new GridLayout(6, 2, 5, 5));

        fields.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        fields.add(txtFirstName);

        fields.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        fields.add(txtLastName);

        fields.add(new JLabel("Email (@psu.edu):"));
        txtEmail = new JTextField();
        fields.add(txtEmail);

        fields.add(new JLabel("Phone (10 digits):"));
        txtPhone = new JTextField();
        fields.add(txtPhone);

        fields.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        fields.add(txtPassword);

        fields.add(new JLabel("Confirm Password:"));
        txtConfirm = new JPasswordField();
        fields.add(txtConfirm);

        panel.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Cancel");
        JButton btnRegister = new JButton("Register");
        buttons.add(btnCancel);
        buttons.add(btnRegister);

        panel.add(buttons, BorderLayout.SOUTH);

        add(panel);

        btnCancel.addActionListener(e -> dispose());
        btnRegister.addActionListener(e -> onRegister());
    }

    private void onRegister() {
        String first = txtFirstName.getText().trim();
        String last = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try {
            authService.registerEmployee(first, last, email, phone, pass);
            JOptionPane.showMessageDialog(this, "Employee registered successfully.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
