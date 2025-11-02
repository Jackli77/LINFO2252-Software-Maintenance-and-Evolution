package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import linfo2252.controller.Controller;

public class TimeView extends JPanel {

    private Controller controller;
    private JLabel dateLabel;

    public TimeView(Controller controller) {
        this.controller = controller;

        setLayout(new FlowLayout());

        dateLabel = new JLabel();
        updateDate(controller.getModel().getCurrentDate());

        JButton advanceDayBtn = new JButton("Advance One Day");
        advanceDayBtn.addActionListener(e -> controller.onAdvanceDay());

        JButton advanceWeekBtn = new JButton("Advance One Week");
        advanceWeekBtn.addActionListener(e -> controller.onAdvanceWeek());

        add(new JLabel("Current Date: "));
        add(dateLabel);
        
        JPanel buttons = new JPanel();
        buttons.add(advanceDayBtn);
        buttons.add(advanceWeekBtn);
        
        add(buttons, BorderLayout.SOUTH);
    }

    public void updateDate(LocalDate newDate) {
        dateLabel.setText(newDate.toString());
    }
}
