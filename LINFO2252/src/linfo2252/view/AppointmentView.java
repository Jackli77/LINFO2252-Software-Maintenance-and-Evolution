package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import java.util.stream.IntStream;

public class AppointmentView extends JPanel {

    private Controller controller;
    private DefaultListModel<Appointment> listModel;
    private JList<Appointment> list;

    public AppointmentView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

     // ===== DATE/TIME PICKERS WITH DEFAULT VALUES =====

     // Get current date/time
     LocalDateTime now = controller.getModel().getCurrentDateTime(); 
     // or LocalDateTime.now() if not tied to model's simulated time

     int currentYear = now.getYear();
     int currentMonth = now.getMonthValue();
     int currentDay = now.getDayOfMonth();
     int currentHour = now.getHour();
     int currentMinute = now.getMinute();

     // Year: current year → +5 future years
     JComboBox<Integer> yearBox = new JComboBox<>(
             IntStream.range(currentYear, currentYear + 6).boxed().toArray(Integer[]::new)
     );
     yearBox.setSelectedItem(currentYear);

     // Month
     JComboBox<Integer> monthBox = new JComboBox<>(
             IntStream.range(1, 13).boxed().toArray(Integer[]::new)
     );
     monthBox.setSelectedItem(currentMonth);

     // Day (we will refill this based on year + month)
     JComboBox<Integer> dayBox = new JComboBox<>();

     // Utility to repopulate days correctly
     Runnable updateDays = () -> {
         dayBox.removeAllItems();
         int y = (Integer) yearBox.getSelectedItem();
         int m = (Integer) monthBox.getSelectedItem();
         int maxDay = java.time.YearMonth.of(y, m).lengthOfMonth();
         for (int d = 1; d <= maxDay; d++) dayBox.addItem(d);
         if (currentDay <= maxDay) dayBox.setSelectedItem(currentDay);
     };
     updateDays.run();

     // Update day count when month or year changes
     yearBox.addActionListener(e -> updateDays.run());
     monthBox.addActionListener(e -> updateDays.run());

     // Time
     JComboBox<Integer> hourBox = new JComboBox<>(
             IntStream.range(0, 24).boxed().toArray(Integer[]::new)
     );
     hourBox.setSelectedItem(currentHour);

     JComboBox<Integer> minuteBox = new JComboBox<>(new Integer[]{0, 15, 30, 45});
     minuteBox.setSelectedItem((currentMinute / 15) * 15); // round to nearest quarter hour

        JTextField typeField = new JTextField("Consultation");
        JTextField deptField = new JTextField("General");

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.of(
                    (Integer) yearBox.getSelectedItem(),
                    (Integer) monthBox.getSelectedItem(),
                    (Integer) dayBox.getSelectedItem()
                );

                LocalDateTime dateTime = date.atTime(
                    (Integer) hourBox.getSelectedItem(),
                    (Integer) minuteBox.getSelectedItem()
                );

                controller.addAppointment(dateTime, typeField.getText(), deptField.getText());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton randomBtn = new JButton("Simulate Random Event");
        randomBtn.addActionListener(e -> controller.createRandomAppointment());

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> {
            Appointment a = list.getSelectedValue();
            if(a != null) controller.removeAppointment(a);
        });

        // Layout form
        form.add(new JLabel("Year:")); form.add(yearBox);
        form.add(new JLabel("Month:")); form.add(monthBox);
        form.add(new JLabel("Day:")); form.add(dayBox);
        form.add(new JLabel("Hour:")); form.add(hourBox);
        form.add(new JLabel("Minute:")); form.add(minuteBox);

        form.add(new JLabel("Type:")); form.add(typeField);
        form.add(new JLabel("Department:")); form.add(deptField);

        JPanel buttons = new JPanel();
        
        buttons.add(addBtn);
        buttons.add(removeBtn);
        buttons.add(randomBtn);

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
