package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import linfo2252.controller.Controller;

public class TimeControlPanel extends JPanel {

    private JLabel dateLabel;

    public TimeControlPanel(Controller controller) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton dayBtn = new JButton("Advance 1 Day");
        JButton weekBtn = new JButton("Advance 1 Week");
        dateLabel = new JLabel("Current Date: " + controller.getModel().getCurrentDate());

        dayBtn.addActionListener(e -> controller.onAdvanceDay());
        weekBtn.addActionListener(e -> controller.onAdvanceWeek());

        add(dayBtn);
        add(weekBtn);
        add(dateLabel);
    }

    public void updateDate(String string) {
        dateLabel.setText("Current Date: " + string);
    }
}
