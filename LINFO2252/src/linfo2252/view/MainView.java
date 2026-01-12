package linfo2252.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import linfo2252.controller.Controller;
import linfo2252.model.Appointment;
import linfo2252.observer.Observer;

/**
 * The primary container for the Graphical User Interface (GUI).
 * <p>
 * This class serves as the main application window (`JFrame`). It manages:
 * <ul>
 * <li>The navigation sidebar for switching between different modules.</li>
 * <li>The central content area where specific views (Appointments, History, Profile) are rendered.</li>
 * <li>The global menu bar for real-time feature toggling.</li>
 * </ul>
 * It implements {@link Observer} to automatically enable/disable UI elements based on the system's feature state.
 */
public class MainView extends JFrame implements Observer {

    private final Controller controller;
    
    // Layout Components
    private JPanel contentPanel;
    private TimeControlPanel timeControlPanel;

    // Sub-Views
    private WelcomeView welcomeView;
    private AppointmentView appointmentView;
    private AppointmentHistoryView historyView;
    private UserView userView;

    // Navigation Controls
    private JButton homeBtn;
    private JButton apptBtn;
    private JButton historyBtn;
    private JButton userBtn;
    
    private JMenu featuresMenu;

    /**
     * Initializes the main window, sets up the layout, and registers as an observer.
     *
     * @param controller The application controller used for navigation and actions.
     */
    public MainView(Controller controller) {
        this.controller = controller;
        
        // Register this view as an observer of the FeatureManager
        // This ensures the GUI updates instantly when features are toggled via Console
        controller.getModel().getFeatureManager().addObserver(this);

        setTitle("Smart Medical Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        
        createMenuBar();
        initUI();
        
        setVisible(true);
    }

    // ==========================================
    // 1. UI INITIALIZATION & LAYOUT
    // ==========================================

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        featuresMenu = new JMenu("Features / Configuration");
        
        // Populate initially
        refreshFeatureMenu();
        
        menuBar.add(featuresMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Dynamically rebuilds the feature menu based on available system features.
     * Called whenever the configuration changes.
     */
    private void refreshFeatureMenu() {
        featuresMenu.removeAll();
        var fm = controller.getModel().getFeatureManager();
        
        for (String featureName : fm.getAvailableFeatures()) {
            boolean isActive = fm.getFeature(featureName).isActive();   
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(featureName, isActive);        
            
            // Action Listener to toggle features directly from the menu
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
        // --- Sidebar Navigation ---
        JPanel sidebar = new JPanel(new GridLayout(0, 1));
        
        homeBtn = new JButton("Home / Dashboard");
        apptBtn = new JButton("Appointments");
        historyBtn = new JButton("History");
        userBtn = new JButton("User Profile");

        // Attach Navigation Actions
        homeBtn.addActionListener(e -> showWelcomeView());
        apptBtn.addActionListener(e -> showAppointmentView());
        historyBtn.addActionListener(e -> showHistoryView());
        userBtn.addActionListener(e -> showUserView());

        sidebar.add(homeBtn);
        sidebar.add(apptBtn);
        sidebar.add(historyBtn);
        sidebar.add(userBtn);

        // --- Main Content Area ---
        contentPanel = new JPanel(new BorderLayout());
        timeControlPanel = new TimeControlPanel(controller);
        add(timeControlPanel, BorderLayout.SOUTH);

        // Initialize Sub-Views
        welcomeView = new WelcomeView(controller);
        appointmentView = new AppointmentView(controller);
        historyView = new AppointmentHistoryView(controller);
        userView = new UserView(controller);

        // --- Final Assembly ---
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Initial Data Load
        updateAppointmentView(controller.getModel().getAppointments());
        updateHistoryView(controller.getModel().getAppointmentHistory());
        updateDateDisplay(controller.getModel().getCurrentDateTime());
        
        refreshSidebarState();
        showWelcomeView(); 
    }
    
    /**
     * Updates the enabled/disabled state of sidebar buttons based on active features.
     */
    private void refreshSidebarState() {
        boolean apptActive = controller.isFeatureActive("AppointmentManagement");
        boolean histActive = controller.isFeatureActive("HistoryTracking");
        boolean timeActive = controller.isFeatureActive("TimeSimulation");
        boolean userActive = controller.isFeatureActive("UserProfile");
        
        userBtn.setEnabled(userActive);
        
        apptBtn.setEnabled(apptActive);
        apptBtn.setToolTipText(apptActive ? "Manage Appointments" : "Feature Disabled");
        
        historyBtn.setEnabled(histActive);   
        
        timeControlPanel.setControlsEnabled(timeActive);
    }

    // ==========================================
    // 2. NAVIGATION LOGIC
    // ==========================================

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

    // ==========================================
    // 3. DATA REFRESH METHODS
    // ==========================================

    public void updateDateDisplay(LocalDateTime date) {
        timeControlPanel.updateDate(date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
    }

    public void updateAppointmentView(List<Appointment> list) {
        appointmentView.updateAppointmentList(list);
    }

    public void updateHistoryView(List<Appointment> list) {
        historyView.updateHistory(list);
    }

    // ==========================================
    // 4. OBSERVER IMPLEMENTATION
    // ==========================================

    /**
     * Called automatically when the FeatureManager notifies of a state change.
     * Updates the menu, sidebar buttons, and handles "forced navigation" if the
     * user is currently viewing a disabled feature.
     */
    @Override
    public void update() {
        refreshFeatureMenu();
        refreshSidebarState();
        
        // If the current view's feature was just disabled, kick user back to profile/home
        if (!apptBtn.isEnabled() && contentPanel.isAncestorOf(appointmentView)) {
            showUserView();
            JOptionPane.showMessageDialog(this, "The feature for the current page was deactivated by an admin.");
        }
    }
}