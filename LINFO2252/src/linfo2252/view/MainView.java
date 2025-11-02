package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import linfo2252.controller.Controller;

public class MainView extends JFrame {

    private Controller controller;
    private JPanel contentPanel;

    private TimeView timeView;
    private AppointmentView appointmentView;
    private AppointmentHistoryView historyView;


    public MainView(Controller controller) {
        this.controller = controller;
        controller.setMainView(this);

        setTitle("Smart Medical Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel sidebar = new JPanel(new GridLayout(0, 1));
        JButton timeBtn = new JButton("Time");
        JButton apptBtn = new JButton("Appointments");
        JButton historyBtn = new JButton("History");

        timeBtn.addActionListener(e -> showTimeView());
        apptBtn.addActionListener(e -> showAppointmentView());
        historyBtn.addActionListener(e -> showHistoryView());

        sidebar.add(timeBtn);
        sidebar.add(apptBtn);
        sidebar.add(historyBtn);

        contentPanel = new JPanel(new BorderLayout());

        // Create subviews
        timeView = new TimeView(controller);
        appointmentView = new AppointmentView(controller);
        historyView = new AppointmentHistoryView(controller);


        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        updateAppointmentView(controller.getModel().getAppointments());
        updateHistoryView(controller.getModel().getAppointmentHistory());
        updateTimeView();

        showTimeView();
    }

    private void showTimeView() {
        contentPanel.removeAll();
        contentPanel.add(timeView, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private void showAppointmentView() {
        contentPanel.removeAll();
        contentPanel.add(appointmentView, BorderLayout.CENTER);
        revalidate(); repaint();
    }
    
    private void showHistoryView() {
        contentPanel.removeAll();
        historyView.updateHistory(controller.getModel().getAppointmentHistory());
        contentPanel.add(historyView, BorderLayout.CENTER);
        revalidate(); repaint();
    }


    // ====== Called by Controller when model updates ======
    public void updateTimeView() {
        timeView.updateDate(controller.getModel().getCurrentDate());
    }

    public void updateAppointmentView(java.util.List<linfo2252.model.Appointment> list) {
        appointmentView.updateAppointmentList(list);
    }
    
    public void updateHistoryView(java.util.List<linfo2252.model.Appointment> list) {
        historyView.updateHistory(list);
    }

}
