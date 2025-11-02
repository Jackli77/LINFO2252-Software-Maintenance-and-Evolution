package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;

public class AppointmentHistoryView extends JPanel {

    private Controller controller;
    private DefaultListModel<Appointment> listModel;
    private JList<Appointment> list;

    public AppointmentHistoryView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        add(new JLabel("Past Appointments"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void updateHistory(List<Appointment> history) {
        listModel.clear();
        for(Appointment a : history) listModel.addElement(a);
    }
}
