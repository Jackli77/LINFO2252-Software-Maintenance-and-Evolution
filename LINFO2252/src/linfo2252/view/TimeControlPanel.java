package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import linfo2252.controller.Controller;

/**
 * A persistent UI component for managing the simulation timeline.
 * <p>
 * This panel usually resides at the bottom of the main window. It provides controls
 * to "fast-forward" the simulation, allowing users to verify time-dependent behaviors
 * (e.g., appointment expiration) without waiting for real time to pass.
 */
public class TimeControlPanel extends JPanel {

    private JLabel dateLabel;
    private JButton dayBtn;
    private JButton weekBtn;

    /**
     * Constructs the time control bar.
     *
     * @param controller The controller to handle time advancement logic.
     */
    public TimeControlPanel(Controller controller) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Add a top border to visually separate this panel from the main content
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        // Initialize Controls
        dayBtn = new JButton("Advance 1 Day");
        weekBtn = new JButton("Advance 1 Week");
        
        // Initial Date Display
        String dateStr = controller.getModel().getCurrentDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        dateLabel = new JLabel("Current Date: " + dateStr);

        // Bind Actions
        dayBtn.addActionListener(e -> controller.advanceDays(1));
        weekBtn.addActionListener(e -> controller.advanceDays(7));

        add(dayBtn);
        add(weekBtn);
        add(dateLabel);
    }

    /**
     * Updates the date label with the new simulation time.
     *
     * @param dateString The formatted date string to display.
     */
    public void updateDate(String dateString) {
        dateLabel.setText("Current Date: " + dateString);
    }

    /**
     * Enables or disables the buttons based on the 'TimeSimulation' feature flag.
     *
     * @param enabled true to unlock controls; false to gray them out.
     */
    public void setControlsEnabled(boolean enabled) {
        dayBtn.setEnabled(enabled);
        weekBtn.setEnabled(enabled);
        dateLabel.setEnabled(enabled);
    }
}