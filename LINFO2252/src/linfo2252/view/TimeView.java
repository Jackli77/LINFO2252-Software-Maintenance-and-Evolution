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

        JButton nextDayBtn = new JButton("Next Day");
        nextDayBtn.addActionListener(e -> controller.onNextDayButton());

        add(new JLabel("Current Date: "));
        add(dateLabel);
        add(nextDayBtn);
    }

    public void updateDate(LocalDate newDate) {
        dateLabel.setText(newDate.toString());
    }
}
