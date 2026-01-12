package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import linfo2252.controller.Controller;

public class WelcomeView extends JPanel {

    public WelcomeView(Controller controller) {
        setLayout(new GridBagLayout()); 
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Smart Medical Appointment Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        add(titleLabel);
    }
    public void refresh() {}
}