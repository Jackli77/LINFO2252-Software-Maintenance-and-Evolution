package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import linfo2252.controller.Controller;
import linfo2252.model.Appointment;

public class AppointmentView extends JPanel {

    private Controller controller;
    private DefaultListModel<Appointment> listModel;
    private JList<Appointment> list;

    public AppointmentView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField dateField = new JTextField("2025-01-01T10:00");
        JTextField typeField = new JTextField("Consultation");
        JTextField deptField = new JTextField("General");

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> controller.onAddAppointment(
                LocalDateTime.parse(dateField.getText()),
                typeField.getText(),
                deptField.getText()
        ));

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> {
            Appointment a = list.getSelectedValue();
            if(a != null) controller.onRemoveAppointment(a);
        });

        form.add(new JLabel("Date & Time:"));
        form.add(dateField);
        form.add(new JLabel("Type:"));
        form.add(typeField);
        form.add(new JLabel("Department:"));
        form.add(deptField);

        JPanel buttons = new JPanel();
        buttons.add(addBtn);
        buttons.add(removeBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        // ===== LIST =====
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void updateAppointmentList(java.util.List<Appointment> appointments) {
        listModel.clear();
        for(Appointment a : appointments) listModel.addElement(a);
    }
}
