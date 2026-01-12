package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.observer.Observer;

public class MainView extends JFrame implements Observer {

    private Controller controller;
    private JPanel contentPanel;
    private TimeControlPanel timeControlPanel;

    private WelcomeView welcomeView;
    private AppointmentView appointmentView;
    private AppointmentHistoryView historyView;
    private UserView userView;

    private JButton homeBtn;
    private JButton apptBtn;
    private JButton historyBtn;
    private JButton userBtn;
    
    private JMenu featuresMenu;

    public MainView(Controller controller) {
        this.controller = controller;
        controller.getModel().getFeatureManager().addObserver(this);

        setTitle("Smart Medical Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
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

    private void refreshFeatureMenu() {
        featuresMenu.removeAll();
        var fm = controller.getModel().getFeatureManager();
        
        for (String featureName : fm.getAvailableFeatures()) {
            boolean isActive = fm.getFeature(featureName).isActive();   
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(featureName, isActive);        
            item.addActionListener(e -> {
                String[] target = { featureName };
                if (item.isSelected()) {
                    controller.activate(null, target);
                } else {
                    controller.activate(target, null);
                }
            });
            
            featuresMenu.add(item);
        }
    }

    private void initUI() {
        JPanel sidebar = new JPanel(new GridLayout(0, 1));
        
        // 1. Initialize Buttons
        homeBtn = new JButton("Home / Dashboard");
        apptBtn = new JButton("Appointments");
        historyBtn = new JButton("History");
        userBtn = new JButton("User Profile");

        // 2. Add Actions
        homeBtn.addActionListener(e -> showWelcomeView());
        apptBtn.addActionListener(e -> showAppointmentView());
        historyBtn.addActionListener(e -> showHistoryView());
        userBtn.addActionListener(e -> showUserView());

        // 3. Add to Sidebar (Home at top)
        sidebar.add(homeBtn);
        sidebar.add(apptBtn);
        sidebar.add(historyBtn);
        sidebar.add(userBtn);

        contentPanel = new JPanel(new BorderLayout());
        timeControlPanel = new TimeControlPanel(controller);
        add(timeControlPanel, BorderLayout.SOUTH);

        // 4. Initialize Views
        welcomeView = new WelcomeView(controller); // <--- NEW
        appointmentView = new AppointmentView(controller);
        historyView = new AppointmentHistoryView(controller);
        userView = new UserView(controller);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        updateAppointmentView(controller.getModel().getAppointments());
        updateHistoryView(controller.getModel().getAppointmentHistory());
        updateDateDisplay(controller.getModel().getCurrentDateTime());
        refreshSidebarState();
        showWelcomeView(); 
    }
    
    private void refreshSidebarState() {
        boolean apptActive = controller.isFeatureActive("AppointmentManagement");
        boolean histActive = controller.isFeatureActive("HistoryTracking");
        boolean timeActive = controller.isFeatureActive("TimeSimulation");
        boolean userActive = controller.isFeatureActive("UserProfile");
        userBtn.setEnabled(userActive);
        apptBtn.setEnabled(apptActive);
        historyBtn.setEnabled(histActive);   
        apptBtn.setToolTipText(apptActive ? "Manage Appointments" : "Feature Disabled");
        timeControlPanel.setControlsEnabled(timeActive);
    }

 // ================= NAVIGATION =================

    public void showWelcomeView() {
        contentPanel.removeAll();
        welcomeView.refresh(); // Fetch fresh name/stats
        contentPanel.add(welcomeView, BorderLayout.CENTER);
        revalidate(); repaint();
        
        controller.logUserAction("Navigate", "WelcomeDashboard");
    }

    public void showAppointmentView() {
        if (!controller.isFeatureActive("AppointmentManagement")) return;
        contentPanel.removeAll();
        contentPanel.add(appointmentView, BorderLayout.CENTER);
        revalidate(); repaint();
        controller.logUserAction("Navigate", "Appointments");
    }

    public void showHistoryView() {
        if (!controller.isFeatureActive("HistoryTracking")) return;
        contentPanel.removeAll();
        historyView.updateHistory(controller.getModel().getAppointmentHistory());
        contentPanel.add(historyView, BorderLayout.CENTER);
        revalidate(); repaint();
        controller.logUserAction("Navigate", "History");
    }

    public void showUserView() {
        contentPanel.removeAll();
        userView.updateUserInfo(controller.getModel().getUserProfile());
        contentPanel.add(userView, BorderLayout.CENTER);
        revalidate(); repaint();
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
    @Override
    public void update() {
        refreshFeatureMenu();
        refreshSidebarState();
        if (!apptBtn.isEnabled() && contentPanel.isAncestorOf(appointmentView)) {
            showUserView();
            JOptionPane.showMessageDialog(this, "The feature for the current page was deactivated.");
        }
    }
}