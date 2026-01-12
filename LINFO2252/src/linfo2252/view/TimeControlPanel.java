package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import linfo2252.controller.Controller;

public class TimeControlPanel extends JPanel {

    private JLabel dateLabel;
    private JButton dayBtn;   // Promoted to field
    private JButton weekBtn;  // Promoted to field

    public TimeControlPanel(Controller controller) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        dayBtn = new JButton("Advance 1 Day");
        weekBtn = new JButton("Advance 1 Week");
        
        // Initial date set
        String dateStr = controller.getModel().getCurrentDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        dateLabel = new JLabel("Current Date: " + dateStr);

        dayBtn.addActionListener(e -> controller.advanceDays(1));
        weekBtn.addActionListener(e -> controller.advanceDays(7));

        add(dayBtn);
        add(weekBtn);
        add(dateLabel);
    }

    public void updateDate(String string) {
        dateLabel.setText("Current Date: " + string);
    }

    /**
     * NEW: Enables or disables the time controls based on feature status
     */
    public void setControlsEnabled(boolean enabled) {
        dayBtn.setEnabled(enabled);
        weekBtn.setEnabled(enabled);
        dateLabel.setEnabled(enabled); // Optional: Grays out text too
    }
}