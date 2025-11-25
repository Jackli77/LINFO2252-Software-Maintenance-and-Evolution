package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import linfo2252.controller.Controller;

public class MainView extends JFrame {

    private Controller controller;
    private JPanel contentPanel;
    private TimeControlPanel timeControlPanel;


    private AppointmentView appointmentView;
    private AppointmentHistoryView historyView;
    private UserView userView;


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

    private void logState(String pageName, String action) {
        // get the feature manager from the model
        var featureManager = controller.getModel().getFeatureManager();
        featureManager.logSystemState(action, pageName);
    }


    private void initUI() {
        JPanel sidebar = new JPanel(new GridLayout(0, 1));
        JButton apptBtn = new JButton("Appointments");
        JButton historyBtn = new JButton("History");
        JButton userBtn = new JButton("User Profile");

        setLayout(new BorderLayout());

        apptBtn.addActionListener(e -> showAppointmentView());
        historyBtn.addActionListener(e -> showHistoryView());
        userBtn.addActionListener(e -> showUserView());

        sidebar.add(apptBtn);
        sidebar.add(historyBtn);
        sidebar.add(userBtn);

        contentPanel = new JPanel(new BorderLayout());
        
        timeControlPanel = new TimeControlPanel(controller);
        add(timeControlPanel, BorderLayout.SOUTH);

        appointmentView = new AppointmentView(controller);
        historyView = new AppointmentHistoryView(controller);
        userView = new UserView(controller);

        
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        updateAppointmentView(controller.getModel().getAppointments());
        updateHistoryView(controller.getModel().getAppointmentHistory());
        
        showAppointmentView();

    }

    private void showAppointmentView() {
        contentPanel.removeAll();
        contentPanel.add(appointmentView, BorderLayout.CENTER);
        revalidate(); repaint();

        logState("APPOINTMENTS", "switchPage");
    }

    private void showHistoryView() {
        contentPanel.removeAll();
        historyView.updateHistory(controller.getModel().getAppointmentHistory());
        contentPanel.add(historyView, BorderLayout.CENTER);
        revalidate(); repaint();

        logState("HISTORY", "switchPage");
    }

    private void showUserView() {
        contentPanel.removeAll();
        userView.updateUserInfo(controller.getModel().getUserProfile());
        contentPanel.add(userView, BorderLayout.CENTER);
        revalidate(); repaint();

        logState("USER_PROFILE", "switchPage");
    }



    // ====== Called by Controller when model updates ======
    public void updateDateDisplay(LocalDateTime date) {
        timeControlPanel.updateDate(date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm")));
    }


    public void updateAppointmentView(java.util.List<linfo2252.model.Appointment> list) {
        appointmentView.updateAppointmentList(list);
    }
    
    public void updateHistoryView(java.util.List<linfo2252.model.Appointment> list) {
        historyView.updateHistory(list);
    }

}
