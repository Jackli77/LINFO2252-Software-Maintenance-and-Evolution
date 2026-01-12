package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.IntStream;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;

/**
 * A Swing panel providing the interface for managing scheduled appointments.
 * <p>
 * This view allows users to:
 * <ul>
 * <li>Create new appointments via a detailed date/time form.</li>
 * <li>Trigger random appointment generation for simulation/testing.</li>
 * <li>View the list of currently active (future) appointments.</li>
 * <li>Remove existing appointments.</li>
 * </ul>
 */
public class AppointmentView extends JPanel {

    private final Controller controller;
    
    // UI Components
    private final DefaultListModel<Appointment> listModel;
    private JList<Appointment> list = new JList<Appointment>();

    /**
     * Constructs the appointment management interface.
     * Initializes the form inputs, action buttons, and the display list.
     *
     * @param controller The central controller for handling data operations.
     */
    public AppointmentView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // 1. Initialize Form Components
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        // --- Date & Time Selection Logic ---
        // Get current simulation time to populate defaults
        LocalDateTime now = controller.getModel().getCurrentDateTime();
        
        // Year: Current year + next 5 years
        JComboBox<Integer> yearBox = new JComboBox<>(
            IntStream.range(now.getYear(), now.getYear() + 6).boxed().toArray(Integer[]::new)
        );
        yearBox.setSelectedItem(now.getYear());

        // Month: 1-12
        JComboBox<Integer> monthBox = new JComboBox<>(
            IntStream.range(1, 13).boxed().toArray(Integer[]::new)
        );
        monthBox.setSelectedItem(now.getMonthValue());

        // Day: Dynamic (populated by updateDays runnable)
        JComboBox<Integer> dayBox = new JComboBox<>();

        // Helper logic to correct the 'Day' dropdown based on selected Month/Year
        // (e.g., prevents selecting "Feb 30")
        Runnable updateDays = () -> {
            dayBox.removeAllItems();
            int y = (Integer) yearBox.getSelectedItem();
            int m = (Integer) monthBox.getSelectedItem();
            int maxDay = YearMonth.of(y, m).lengthOfMonth();
            
            for (int d = 1; d <= maxDay; d++) dayBox.addItem(d);
            
            // Try to keep the previously selected day if valid
            if (now.getDayOfMonth() <= maxDay) {
                dayBox.setSelectedItem(now.getDayOfMonth());
            }
        };

        // Initialize days and attach listeners
        updateDays.run();
        yearBox.addActionListener(e -> updateDays.run());
        monthBox.addActionListener(e -> updateDays.run());

        // Time Selectors
        JComboBox<Integer> hourBox = new JComboBox<>(
            IntStream.range(0, 24).boxed().toArray(Integer[]::new)
        );
        hourBox.setSelectedItem(now.getHour());

        JComboBox<Integer> minuteBox = new JComboBox<>(new Integer[]{0, 15, 30, 45});
        // Round current minute to nearest quarter
        minuteBox.setSelectedItem((now.getMinute() / 15) * 15);

        // Text Fields
        JTextField typeField = new JTextField("Consultation");
        JTextField deptField = new JTextField("General");

        // --- Action Buttons ---
        
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            try {
                // Reconstruct LocalDate from UI components
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
                JOptionPane.showMessageDialog(this, "Invalid date selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton randomBtn = new JButton("Simulate Random Event");
        randomBtn.setToolTipText("Generates a random appointment 1-7 days in the future");
        randomBtn.addActionListener(e -> controller.createRandomAppointment());

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> {
            Appointment a = list.getSelectedValue();
            if (a != null) controller.removeAppointment(a);
        });

        // --- Layout Assembly ---
        
        // Add inputs to Form Grid
        formPanel.add(new JLabel("Year:"));   formPanel.add(yearBox);
        formPanel.add(new JLabel("Month:"));  formPanel.add(monthBox);
        formPanel.add(new JLabel("Day:"));    formPanel.add(dayBox);
        formPanel.add(new JLabel("Hour:"));   formPanel.add(hourBox);
        formPanel.add(new JLabel("Minute:")); formPanel.add(minuteBox);
        formPanel.add(new JLabel("Type:"));   formPanel.add(typeField);
        formPanel.add(new JLabel("Department:")); formPanel.add(deptField);

        // Button Bar
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(randomBtn);

        // Top Container (Form + Buttons)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // 2. Initialize List View
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    /**
     * Refreshes the appointment list UI with the latest data from the model.
     *
     * @param appointments The list of active appointments to display.
     */
    public void updateAppointmentList(List<Appointment> appointments) {
        listModel.clear();
        for (Appointment a : appointments) {
            listModel.addElement(a);
        }
    }
}