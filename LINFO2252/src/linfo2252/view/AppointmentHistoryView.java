package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;

/**
 * A specialized Swing view for displaying past medical appointments.
 * <p>
 * This panel renders a read-only list of historical appointment records.
 * It is updated automatically by the {@link linfo2252.view.MainView} whenever
 * the system time advances past an appointment's scheduled date.
 */
public class AppointmentHistoryView extends JPanel {

    private final Controller controller;
    private final DefaultListModel<Appointment> listModel;
    private final JList<Appointment> list;

    /**
     * Constructs the history view panel.
     *
     * @param controller The main application controller (used for potential navigation or details lookup).
     */
    public AppointmentHistoryView(Controller controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Initialize the list component with a default model
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        // Header Label
        JLabel titleLabel = new JLabel("Past Appointments");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    /**
     * Refreshes the list display with new historical data.
     * This method is thread-safe for Swing event dispatching.
     *
     * @param history The updated list of past appointments to display.
     */
    public void updateHistory(List<Appointment> history) {
        listModel.clear();
        if (history != null) {
            for (Appointment a : history) {
                listModel.addElement(a);
            }
        }
    }
}