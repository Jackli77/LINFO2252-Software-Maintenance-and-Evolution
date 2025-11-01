package linfo2252.view;

import javax.swing.*;
import java.awt.*;

public class UIView {
    private JFrame frame;
    private JTextArea logArea;
    private JPanel panel;

    public UIView() {
        frame = new JFrame("Smart Appointment Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        panel = new JPanel();
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);

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
