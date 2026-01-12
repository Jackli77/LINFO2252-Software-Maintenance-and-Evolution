package linfo2252.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import linfo2252.controller.Controller;
import linfo2252.model.InsuranceLevel;
import linfo2252.model.UserProfile;

/**
 * A form-based interface for managing user profile details.
 * <p>
 * This view demonstrates <b>Data-Driven Adaptability</b>:
 * <ul>
 * <li>It allows editing of personal information and insurance status.</li>
 * <li>It visually transforms (Gold Border/Theme) when the user upgrades to a Premium plan.</li>
 * </ul>
 */
public class UserView extends JPanel {

    private final Controller controller;
    
    // Form Components
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JComboBox<InsuranceLevel> insuranceBox;
    private final JButton saveBtn = new JButton("Save Changes");
    private final JLabel statusLabel = new JLabel(" ");

    /**
     * Constructs the user profile form using a flexible GridBagLayout.
     *
     * @param controller The application controller for saving updates.
     */
    public UserView(Controller controller) {
        this.controller = controller;
        this.setLayout(new GridBagLayout());
        
        // Initialize Dropdown with Enum values
        insuranceBox = new JComboBox<>(InsuranceLevel.values());

        initForm();
    }

    /**
     * Arranges the UI components into a structured grid.
     */
    private void initForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel title = new JLabel("User Profile Settings");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Span across columns
        add(title, gbc);

        // --- Input Fields ---
        // Helper method 'addField' simplifies repetitive grid positioning
        addField("Full Name:", nameField, 1, gbc);
        addField("Email Address:", emailField, 2, gbc);
        addField("Phone Number:", phoneField, 3, gbc);
        addField("Insurance Plan:", insuranceBox, 4, gbc);

        // --- Save Button ---
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.EAST;
        
        // Style the button
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
            statusLabel.setForeground(new Color(0, 100, 0)); // Dark Green
        });
        
        add(saveBtn, gbc);

        // --- Status Feedback ---
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(statusLabel, gbc);
    }

    /**
     * Helper to place a Label + Component pair on the grid.
     */
    private void addField(String labelText, JComponent field, int y, GridBagConstraints gbc) {
        gbc.gridwidth = 1;
        
        // Label Column
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(labelText), gbc);
        
        // Input Column
        gbc.gridx = 1;
        add(field, gbc);
    }

    /**
     * Refreshes the form with the latest user data and applies visual themes.
     *
     * @param user The current user profile object.
     */
    public void updateUserInfo(UserProfile user) {
        // Populate Fields
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        insuranceBox.setSelectedItem(user.getInsurance());

        // Apply Adaptive Theme based on Insurance Level
        if (user.getInsurance() == InsuranceLevel.PREMIUM) {
            // Gold Theme for Premium Users
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2), 
                " ⭐ PREMIUM MEMBER ⭐ ", 
                TitledBorder.CENTER, 
                TitledBorder.TOP
            ));
            setBackground(new Color(255, 250, 240)); // Floral White
        } else {
            // Standard Look
            setBorder(BorderFactory.createTitledBorder(" Member Details "));
            setBackground(null);
        }
        
        // Reset status message on fresh load
        statusLabel.setText(" ");
    }
}