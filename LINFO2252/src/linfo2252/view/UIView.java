package linfo2252.view;

import linfo2252.controller.UIController;
import javax.swing.*;
import java.awt.*;

public class UIView {
    private JFrame frame;
    private JTextArea logArea;
    private JPanel panel;

    public UIView(UIController controller) {
        frame = new JFrame("Smart Appointment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // Create log area
        logArea = new JTextArea(6, 30);
        logArea.setEditable(false);

        // ---- NEW BUTTON ----
        JButton nextDayBtn = new JButton("Next Day");
        nextDayBtn.addActionListener(e -> {
            controller.advanceDay();
            appendToLog("Day advanced to: " + controller.getModel().getTES().getDate());
        });
        panel.add(nextDayBtn);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void appendToLog(String msg) {
        logArea.append(msg + "\n");
    }

    public void refresh() {
        frame.repaint();
        frame.revalidate();
    }
}
