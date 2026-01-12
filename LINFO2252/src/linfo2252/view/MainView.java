package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.observer.Observer; // 1. Import Observer

public class MainView extends JFrame implements Observer { // 2. Implement Observer

    private Controller controller;
    private JPanel contentPanel;
    private TimeControlPanel timeControlPanel;

    // Views
    private AppointmentView appointmentView;
    private AppointmentHistoryView historyView;
    private UserView userView;

    // Sidebar Buttons (Promoted to fields so we can disable them)
    private JButton apptBtn;
    private JButton historyBtn;
    private JButton userBtn;
    
    // Feature Menu
    private JMenu featuresMenu;

    public MainView(Controller controller) {
        this.controller = controller;
        
        // 3. Register as Observer to FeatureManager
        // This ensures the GUI updates if features change via Console
        controller.getModel().getFeatureManager().addObserver(this);

        setTitle("Smart Medical Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Made slightly bigger for better layout
        setLocationRelativeTo(null);
        
        // 4. Create the Menu Bar for Feature Toggling
        createMenuBar();

        initUI();
        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        featuresMenu = new JMenu("Features / Configuration");
        
        // Populate initially
        refreshFeatureMenu();
        
        menuBar.add(featuresMenu);
        setJMenuBar(menuBar);
    }

    /**
     * dynamically builds checkboxes for every feature in the system
     */
    private void refreshFeatureMenu() {
        featuresMenu.removeAll();
        var fm = controller.getModel().getFeatureManager();
        
        for (String featureName : fm.getAvailableFeatures()) {
            boolean isActive = fm.getFeature(featureName).isActive();
            
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(featureName, isActive);
            
            // Add Action Listener to toggle feature via Controller
            item.addActionListener(e -> {
                String[] target = { featureName };
                if (item.isSelected()) {
                    controller.activate(null, target); // Activate
                } else {
                    controller.activate(target, null); // Deactivate
                }
            });
            
            featuresMenu.add(item);
        }
    }

    private void initUI() {
        // Sidebar Setup
        JPanel sidebar = new JPanel(new GridLayout(0, 1));
        apptBtn = new JButton("Appointments");
        historyBtn = new JButton("History");
        userBtn = new JButton("User Profile");

        // Navigation Actions
        apptBtn.addActionListener(e -> showAppointmentView());
        historyBtn.addActionListener(e -> showHistoryView());
        userBtn.addActionListener(e -> showUserView());

        sidebar.add(apptBtn);
        sidebar.add(historyBtn);
        sidebar.add(userBtn);

        // Content Setup
        contentPanel = new JPanel(new BorderLayout());
        timeControlPanel = new TimeControlPanel(controller);
        add(timeControlPanel, BorderLayout.SOUTH);

        appointmentView = new AppointmentView(controller);
        historyView = new AppointmentHistoryView(controller);
        userView = new UserView(controller);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Initial Data Load
        updateAppointmentView(controller.getModel().getAppointments());
        updateHistoryView(controller.getModel().getAppointmentHistory());
        updateDateDisplay(controller.getModel().getCurrentDateTime());

        // 5. Apply Feature Locks (Gray out buttons if features are off)
        refreshSidebarState();

        // 6. DEFAULT VIEW IS NOW USER PROFILE
        showUserView();
    }
    
    /**
     * Enables/Disables sidebar buttons based on active features
     */
    private void refreshSidebarState() {
        boolean apptActive = controller.isFeatureActive("AppointmentManagement");
        boolean histActive = controller.isFeatureActive("HistoryTracking");
        boolean timeActive = controller.isFeatureActive("TimeSimulation");
        apptBtn.setEnabled(apptActive);
        historyBtn.setEnabled(histActive);   
        apptBtn.setToolTipText(apptActive ? "Manage Appointments" : "Feature Disabled");
        timeControlPanel.setControlsEnabled(timeActive);
    }

    // ================= NAVIGATION =================

    public void showAppointmentView() {
        if (!controller.isFeatureActive("AppointmentManagement")) return; // Guard
        
        contentPanel.removeAll();
        contentPanel.add(appointmentView, BorderLayout.CENTER);
        revalidate(); repaint();
        controller.logUserAction("Navigate", "Appointments");
    }

    public void showHistoryView() {
        if (!controller.isFeatureActive("HistoryTracking")) return; // Guard

        contentPanel.removeAll();
        // Always fetch fresh history
        historyView.updateHistory(controller.getModel().getAppointmentHistory());
        contentPanel.add(historyView, BorderLayout.CENTER);
        revalidate(); repaint();
        controller.logUserAction("Navigate", "History");
    }

    public void showUserView() {
        contentPanel.removeAll();
        // Always fetch fresh profile
        userView.updateUserInfo(controller.getModel().getUserProfile());
        contentPanel.add(userView, BorderLayout.CENTER);
        revalidate(); repaint();
        
        // Log navigation
        controller.logUserAction("Navigate", "UserProfile");
    }

    // ================= UPDATES =================

    public void updateDateDisplay(LocalDateTime date) {
        timeControlPanel.updateDate(date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
    }

    public void updateAppointmentView(List<Appointment> list) {
        appointmentView.updateAppointmentList(list);
    }

    public void updateHistoryView(List<Appointment> list) {
        historyView.updateHistory(list);
    }

    // ================= OBSERVER IMPLEMENTATION =================
    
    /**
     * Triggered when FeatureManager changes state (e.g. via Console or Menu)
     */
    @Override
    public void update() {
        // 1. Re-sync the menu checkboxes (so they match the Console)
        refreshFeatureMenu();
        
        // 2. Enable/Disable Sidebar buttons
        refreshSidebarState();
        
        // 3. If we are currently on a disabled page, force switch to UserProfile
        if (!apptBtn.isEnabled() && contentPanel.isAncestorOf(appointmentView)) {
            showUserView();
            JOptionPane.showMessageDialog(this, "The feature for the current page was deactivated.");
        }
    }
}