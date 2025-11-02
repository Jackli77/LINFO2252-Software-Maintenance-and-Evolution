package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;

public class AppointmentView extends JPanel {

    private Controller controller;
    private DefaultListModel<Appointment> listModel;
    private JList<Appointment> list;

    public AppointmentView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        JPanel top = new JPanel();
        JTextField typeField = new JTextField(10);
        JTextField deptField = new JTextField(10);
        JTextField dateField = new JTextField(10); // yyyy-mm-dd
        JTextField timeField = new JTextField(5);  // HH:mm

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime time = LocalTime.parse(timeField.getText());
                LocalDateTime dateTime = LocalDateTime.of(date, time);

                controller.onAddAppointment(
                        dateTime,
                        typeField.getText(),
                        deptField.getText()
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid format.\nDate: yyyy-mm-dd\nTime: HH:mm");
            }
        });

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> {
            Appointment selected = list.getSelectedValue();
            if(selected != null) controller.onRemoveAppointment(selected);
        });

        top.add(new JLabel("Type:"));
        top.add(typeField);
        top.add(new JLabel("Department:"));
        top.add(deptField);
        top.add(new JLabel("Date yyyy-mm-dd:"));
        top.add(dateField);
        top.add(new JLabel("Time HH:mm:"));
        top.add(timeField);
        top.add(addBtn);
        top.add(removeBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void updateAppointmentList(List<Appointment> appointments) {
        listModel.clear();
        for(Appointment a : appointments) listModel.addElement(a);
    }
}
