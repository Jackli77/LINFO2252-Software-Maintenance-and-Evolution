package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import linfo2252.controller.Controller;

/**
 * The initial landing page / dashboard of the application.
 * <p>
 * This view provides a clean, welcoming entry point for users. It is displayed 
 * immediately after startup or when the user navigates back to "Home".
 */
public class WelcomeView extends JPanel {

    /**
     * Constructs the welcome screen.
     * Uses {@link GridBagLayout} without constraints to perfectly center the content.
     *
     * @param controller The application controller (unused here, but kept for consistency).
     */
    public WelcomeView(Controller controller) {
        // GridBagLayout centers components by default when no constraints are provided
        setLayout(new GridBagLayout()); 
        setBackground(Color.WHITE);

        // Main Title
        JLabel titleLabel = new JLabel("Smart Medical Appointment Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50)); // Dark Gray for professional look
        
        add(titleLabel);
    }

    /**
     * Refreshes the view state.
     * <p>
     * Currently an empty implementation as the welcome screen is static, 
     * but this method ensures consistency with the application's view contract.
     */
    public void refresh() {
        // No dynamic data to update on the welcome screen yet.
    }
}