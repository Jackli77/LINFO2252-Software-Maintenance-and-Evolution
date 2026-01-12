package linfo2252.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import linfo2252.controller.Controller;
import linfo2252.model.InsuranceLevel;
import linfo2252.model.UserProfile;

public class UserView extends JPanel {

    private final Controller controller;
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JComboBox<InsuranceLevel> insuranceBox;
    private final JButton saveBtn = new JButton("Save Changes");
    private final JLabel statusLabel = new JLabel(" ");

    public UserView(Controller controller) {
        this.controller = controller;
        this.setLayout(new GridBagLayout());
        insuranceBox = new JComboBox<>(InsuranceLevel.values());

        initForm();
    }

    private void initForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Fields
        addField("Full Name:", nameField, 1, gbc);
        addField("Email Address:", emailField, 2, gbc);
        addField("Phone Number:", phoneField, 3, gbc);
        addField("Insurance Plan:", insuranceBox, 4, gbc);

        // Save Button
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        
        saveBtn.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        
        saveBtn.addActionListener(e -> {
            controller.updateUserProfile(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                (InsuranceLevel) insuranceBox.getSelectedItem()
            );
            statusLabel.setText("✅ Profile updated successfully!");
            statusLabel.setForeground(new Color(0, 100, 0));
        });
        
        add(saveBtn, gbc);

        // Status Feedback
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(statusLabel, gbc);
    }

    private void addField(String labelText, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        add(field, gbc);
    }

    /**
     * Called by MainView to populate fields when the tab is opened
     */
    public void updateUserInfo(UserProfile user) {
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        insuranceBox.setSelectedItem(user.getInsurance());

        // Visual Flair: Change border/color based on Insurance
        if (user.getInsurance() == InsuranceLevel.PREMIUM) {
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2), 
                " ⭐ PREMIUM MEMBER ⭐ ", TitledBorder.CENTER, TitledBorder.TOP
            ));
            setBackground(new Color(255, 250, 240)); // Floral White
        } else {
            setBorder(BorderFactory.createTitledBorder(" Member Details "));
            setBackground(null); // Default
        }
        
        statusLabel.setText(" "); // Reset status
    }
}